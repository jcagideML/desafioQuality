package com.bootcamp.booking.utils;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class XLSXToJSONConverter {

    /*
    Leo el archivo excel, tal cual lo manda el usuario.
    La intención es levantar el archivo, tal cual lo manda el usuario
    y que sea tarea nuestra procesarlo. De esta forma, CREAMOS VALOR PARA LOS USUARIOS,
    al evitar que ellos deban hacerlo.
     */
    public static List<String> getRowsFromXLSX(String path) {
        List<String> rows = new ArrayList<>();
        File file;
        try {
            file = ResourceUtils.getFile("classpath:" + path);
            FileInputStream fis = new FileInputStream(file);
            XSSFSheet sheet = new XSSFWorkbook(fis).getSheetAt(0);
            StringBuilder aux = new StringBuilder();
            for (Row row : sheet) {
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    switch (cell.getCellTypeEnum()) {
                        case NUMERIC:
                            if (HSSFDateUtil.isCellDateFormatted(cell)) {
                                aux.append(cell.getDateCellValue());
                                break;
                            } else {
                                aux.append(cell.getNumericCellValue());
                                break;
                            }
                        case STRING:
                            aux.append(cell.getStringCellValue());
                            break;

                    }
                    if (cellIterator.hasNext())
                        aux.append(",");
                }
                rows.add(aux.toString());
                aux = new StringBuilder();
            }
            return rows;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
    Creo el archivo JSON. La primer fila son los Headers
    A partir de la segunda fila, empieza a crear los objetos JSON.
    Tanto el metodo anterior como este, estan pensados para que si el día de mañana
    se agregan nuevas columnas al EXCEL, siga funcionando, y solo se deba agregar en el repo
    como se deben interpretar los nuevos campos.
     */
    @SuppressWarnings("unchecked")
    public static void toJSON(List<String> rows, String path) {
        String[] headers = rows.get(0).split(",");
        File file;
        try {
            file = ResourceUtils.getFile("classpath:" + path);
            JSONArray jsonArray = new JSONArray();
            for (int j = 1; j < rows.size(); j++) {
                String[] body = rows.get(j).split(",");
                JSONObject jo = new JSONObject();
                for (int i = 0; i < body.length; i++) {
                    jo.put(headers[i], body[i]);
                }
                jsonArray.add(jo);
            }
            Files.write(file.toPath(), jsonArray.toJSONString().getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}