package document.pdf.load;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import java.awt.image.BufferedImage;

public class PdfImageConvert {

    /**
     * Converte todas as páginas de um arquivo PDF em imagens e salva em um diretório.
     *
     * @param pdfFilePath   O caminho completo do arquivo PDF de entrada.
     * @param outputDirPath O caminho do diretório onde as imagens serão salvas.
     * @param imageFormat   O formato da imagem a ser salva (ex: "png", "jpg").
     * @throws IOException Se ocorrer um erro durante a leitura/escrita do arquivo.
     */
    public void convertPdfToImages(
                File pdfFilePath,
                File outputDirPath,
                String imageFormat,
                String prefix
            ) throws IOException {

        if (!outputDirPath.exists()) {
            outputDirPath.mkdirs();
            //System.out.println("Diretório de saída criado: " + outputDirPath);
        }

        // 2. Carrega o documento PDF
        PDDocument document = null;
        try {
            document = PDDocument.load(pdfFilePath);
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            int numPages = document.getNumberOfPages();
            //System.out.println("Iniciando conversão de " + numPages + " páginas...");

            // 3. Itera sobre cada página
            for (int page = 0; page < numPages; page++) {
                // Converte a página em uma imagem
                // O fator 1.5 é para aumentar a resolução e melhorar a qualidade da imagem
                BufferedImage image = pdfRenderer.renderImageWithDPI(page, 200);

                // Define o nome do arquivo de saída
                String fileName = prefix + (page + 1) + "." + imageFormat;
                File outputFile = new File(outputDirPath.getAbsolutePath(), fileName);

                // Salva a imagem no disco
                ImageIO.write(image, imageFormat, outputFile);
                //System.out.println("Página " + (page + 1) + " salva como " + fileName);
            }

            //System.out.println("Conversão concluída com sucesso!");

        } finally {
            // 4. Fecha o documento para liberar recursos
            if (document != null) {
                document.close();
            }
        }
    }
}
