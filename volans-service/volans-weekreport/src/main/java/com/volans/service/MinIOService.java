package com.volans.service;

import java.io.InputStream;

public interface MinIOService
{

    public String uploadImgFile(String prefix, String filename, InputStream inputStream);

    public String uploadExcelFile(String prefix, String filename, InputStream inputStream);

    public String getDefaultImgPath();
}