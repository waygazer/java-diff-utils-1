/*
 * Copyright 2022 hp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.trejhara;

/**
 *
 * @author hp
 */

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import java.io.FileOutputStream;
import java.io.IOException;

public class DecryptPDF {

    static String owner = "d1e2n3a4b5a6n7k8";
    static String user = "yoge2507";
    static String original = "OS19477942_D1326244.pdf";
    static String destination = "UPOS19477942_D1326244.pdf";

    public static void main(String... args) throws IOException, DocumentException {
        //PdfReader reader = new PdfReader(original, owner.getBytes());
        PdfReader reader = new PdfReader(original, user.getBytes());
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(destination));

        stamper.close();
    }
}