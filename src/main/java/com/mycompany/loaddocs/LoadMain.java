/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.loaddocs;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Path;
import java.nio.file.Paths;
import type_utils.DataTableExcel;
import org.apache.poi.util.IOUtils;
import type_utils.save.SaveTable;
import type_utils.save.SaveTableExcel;
import type_utils.table.*;
import type_utils.FilterFilesPdf;
import document.pdf.load.PdfImageConvert;

/**
 *
 * @author bruno
 */
public class LoadMain {

    public static void main(String[] args) {
        // --- 1. Definições ---
        int NOVO_LIMITE_BYTES = 400 * 1024 * 1024;
        // Aplica o override
        IOUtils.setByteArrayMaxOverride(NOVO_LIMITE_BYTES);

        // Altere estes caminhos conforme a sua necessidade
        String inputDirPdf = "/mnt/dados/2025-11-02 Cartas Toi WhatsApp/Origin/WP TOIS/EXT/EXT WP TOI DE AGO 2024 ATE 10 SET 2025";
        String outputDirectory = "/home/brunoc/Downloads/saida";
        String format = "png"; // ou "jpg"
        // ------------------

        FilterFilesPdf filter = new FilterFilesPdf();
        List<File> filesPdf = filter.getPdfFilesLegacy(inputDirPdf);

        PdfImageConvert converter = new PdfImageConvert();
        try {
            int count = 0;
            for (int i = 1; i <= filesPdf.size(); i++) {
                File file = filesPdf.get(i);
                System.out.println("Convertendo: " + count + "/" + filesPdf.size() + " " + file.getName());

                converter.convertPdfToImages(
                        file,
                        new File(outputDirectory),
                        format,
                        file.getName()
                );
                count++;
            }

        } catch (IOException e) {
            System.err.println("Erro durante o processamento do PDF: " + e.getMessage());
            e.printStackTrace();
        }

    }

}

