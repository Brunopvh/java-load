package type_utils;

import java.util.List;
import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.List;

public class FilterFilesPdf {

    public List<File> getPdfFilesLegacy(String directoryPath) {
        File dir = new File(directoryPath);

        // Define um filtro para aceitar apenas arquivos que terminam com ".pdf"
        FileFilter pdfFilter = new FileFilter() {
            @Override
            public boolean accept(File file) {
                // Converte para minúsculas para garantir que .PDF ou .Pdf também sejam incluídos
                return file.isFile() && file.getName().toLowerCase().endsWith(".pdf");
            }
        };

        File[] files = dir.listFiles(pdfFilter);

        // Retorna a lista, ou uma lista vazia se o diretório for nulo (não existe)
        return files != null ? Arrays.asList(files) : List.of();
    }
}
