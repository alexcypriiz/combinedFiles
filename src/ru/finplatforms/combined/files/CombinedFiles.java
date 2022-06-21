package ru.finplatforms.combined.files;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Collectors;

public class CombinedFiles {
    public static void main(String[] args) throws IOException {
//        Ключ = путь к файлу, значение = имя файла
        Path pathParent = Paths.get(System.getProperty("user.dir")).getParent(); // запуск из корня папки
        Map<String, Path> paths =
                Files.walk(pathParent)
                        .filter(Files::isRegularFile)
                        .filter(p -> p.toString().endsWith(".txt"))
                        .collect(Collectors.toMap(Path::toString, Path::getFileName));

//        сортировка по значению
        Map<String, Path> pathSortedMap = paths.entrySet().stream()
                .sorted(Map.Entry.<String, Path>comparingByValue())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> {
                            throw new AssertionError();
                        },
                        LinkedHashMap::new
                ));

// Запись содержимого файлов в новый файл. При повторном запуске текст будет добавлен к имеющемуся
        for (Map.Entry<String, Path> entry : pathSortedMap.entrySet()) {
            Files.write(Paths.get("/home/alex/combinedFiles/outPutText.txt"), Files.lines(Path.of(entry.getKey())).collect(Collectors.toList()),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);
        }

    }
}
