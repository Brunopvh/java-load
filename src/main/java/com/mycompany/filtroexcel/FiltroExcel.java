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
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import table.read.LoadTable;
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
        
        String ARQUIVO_ENTRADA = "/home/bruno/Downloads/LV 3.xlsx";
        String ARQUIVO_SAIDA = "/home/brunoc/Documentos/BASE/teste.xlsx";
        String NOME_PLANILHA = "Planilha1"; // Nome da planilha a ser lida
        int INDICE_COLUNA_FILTRO = 1; // Coluna B (índice 1, pois começa em 0)
        String VALOR_FILTRO = "87008"; // String para filtrar

        try {

            FileInputStream arquivoEntrada = new FileInputStream(ARQUIVO_ENTRADA);
            LoadTable ld = new LoadTable(arquivoEntrada, NOME_PLANILHA);
            List<String> cols = ld.getColumns();
            System.out.println(cols);
            System.out.println(cols.getFirst());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

