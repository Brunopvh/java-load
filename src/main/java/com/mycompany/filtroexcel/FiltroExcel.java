/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.filtroexcel;

import excel.leitor.ExcelReader;
import excel.leitor.XLSXReader;
import excel.processor.ExcelProcessor;
import java.io.FileInputStream;
import java.io.File;
import java.io.InputStream;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.util.IOUtils;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;


/**
 *
 * @author bruno
 */
public class FiltroExcel {

    public static void main(String[] args) {
        // --- 1. Definições ---
        int NOVO_LIMITE_BYTES = 400 * 1024 * 1024;
        // Aplica o override
        IOUtils.setByteArrayMaxOverride(NOVO_LIMITE_BYTES);
        
        String ARQUIVO_ENTRADA = "/home/brunoc/Documentos/BASE/base-gm.xlsx";
        String ARQUIVO_SAIDA = "/home/brunoc/Documentos/BASE/teste.xlsx";
        String NOME_PLANILHA = "Planilha1"; // Nome da planilha a ser lida
        int INDICE_COLUNA_FILTRO = 1; // Coluna B (índice 1, pois começa em 0)
        String VALOR_FILTRO = "87008"; // String para filtrar

        ExcelReader readerImpl = new XLSXReader();
        ExcelProcessor process = new ExcelProcessor(readerImpl);

        try {
            // --- 2. Leitura do Arquivo Original ---
            FileInputStream arquivoEntrada = new FileInputStream(ARQUIVO_ENTRADA);
            process.filterAndSave(
                    arquivoEntrada,
                    NOME_PLANILHA,
                    INDICE_COLUNA_FILTRO,
                    VALOR_FILTRO,
                    ARQUIVO_SAIDA
            );

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("❌ Erro ao processar o arquivo. Verifique o caminho e as permissões.");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("❌ Ocorreu um erro inesperado: " + e.getMessage());
        }
    }

}

