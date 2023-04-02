package com.example.invoicemaker;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.RestrictionEntry;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;

public class MainActivity extends AppCompatActivity {
    private EditText nameEditText, numberEditText, addressEditText, dateEditText, priceEditText;
    private String name, phone, address, date, price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nameEditText= findViewById(R.id.nameEditText);
        numberEditText= findViewById(R.id.phoneEditText);
        addressEditText= findViewById(R.id.addressEditText);
        dateEditText= findViewById(R.id.invoiceDateEditText);
        priceEditText= findViewById(R.id.priceEditText);
        Button button = findViewById(R.id.button);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = nameEditText.getText().toString();
                phone = numberEditText.getText().toString();
                address = addressEditText.getText().toString();
                date = dateEditText.getText().toString();
                price = priceEditText.getText().toString();

                if (name.length()==0) {
                    nameEditText.setError("Field is must!");
                }
                if(phone.length()==0){
                    numberEditText.setError("Field is must!");
                }
                if(address.length()==0){
                    addressEditText.setError("Field is must!");
                }
                if(date.length()==0){
                    dateEditText.setError("Field is must!");
                }
                if(price.length()==0){
                    priceEditText.setError("Field is must!");
                }
                else {
                    try {
                        createPDF();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    private void createPDF() throws FileNotFoundException {
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString();
        File file = new File(path, "Invoice.pdf");
        OutputStream outputStream = new FileOutputStream(file);

        PdfWriter writer = new PdfWriter(String.valueOf(file));
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument);

        //  COLOR RGB
        DeviceRgb green = new DeviceRgb(60,179,113);
        DeviceRgb white = new DeviceRgb(255,255,255);
        DeviceRgb grey = new DeviceRgb(128,128,128);

        //        TABLE 1

        float[] columnWidth1 = {280, 280};
        Table table1 = new Table(columnWidth1);

        Drawable d1 = getDrawable(R.drawable.logo);
        Bitmap bmp1 = ((BitmapDrawable)d1).getBitmap();
        ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
        bmp1.compress(Bitmap.CompressFormat.JPEG, 100, stream1);
        byte[] bitmapData1 = stream1.toByteArray();

        ImageData imageData1 = ImageDataFactory.create(bitmapData1);
        Image image1 = new Image(imageData1);
        image1.setWidth(100f);

        //  ROW 01
        table1.addCell(new Cell(4, 1).add(image1).setMarginTop(50f).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER));
        table1.addCell(new Cell(4, 1).add(new Paragraph("KALAM PUBLIC\nSCHOOL").setMarginTop(35f).setMarginLeft(106f).setFontSize(20f)).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER));
        //  ROW 02
        table1.addCell(new Cell(3, 1).add(new Paragraph("MANAGED BY\nTAMEER-E-NAU\nWELFARE TRUST").setMarginTop(10f)).setBorder(Border.NO_BORDER));
        table1.addCell(new Cell(3,1).add(new Paragraph("")).setBorder(Border.NO_BORDER));
        //  ROW 03
        table1.addCell(new Cell().add(new Paragraph("\n")).setBorder(Border.NO_BORDER));
        table1.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        //  ROW 04
        table1.addCell(new Cell().add(new Paragraph("Invoice Date:").setBold()).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT));
        table1.addCell(new Cell().add(new Paragraph("Invoice To:").setBold()).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT));
        //  ROW 05
        table1.addCell(new Cell().add(new Paragraph(date)).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT));
        table1.addCell(new Cell().add(new Paragraph(name+"\n"+address+"\n"+phone)).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT));

        //        TABLE 2

        float[] columnWidth2 = {280, 280};
        Table table2 = new Table(columnWidth2);

        //  ROW 01
        table2.addCell(new Cell().add(new Paragraph("DESCRIPTION").setBold().setFontColor(white).setBackgroundColor(green)).setBorder(Border.NO_BORDER));
        table2.addCell(new Cell().add(new Paragraph("PRICE").setBold().setFontColor(white).setBackgroundColor(green)).setBorder(Border.NO_BORDER));
        //  ROW 02
        table2.addCell(new Cell().add(new Paragraph("Donate").setBackgroundColor(grey, 0.3f)).setBorder(Border.NO_BORDER));
        table2.addCell(new Cell().add(new Paragraph(price).setBackgroundColor(grey, 0.3f)).setBorder(Border.NO_BORDER));
        //  ROW 03
        table2.addCell(new Cell(2, 1).add(new Paragraph("\n").setBackgroundColor(grey, 0.3f)).setBorder(Border.NO_BORDER));
        table2.addCell(new Cell(2, 1).add(new Paragraph("\n").setBackgroundColor(grey, 0.3f)).setBorder(Border.NO_BORDER));
        //  ROW 04
        table2.addCell(new Cell(2,1).add(new Paragraph("\n").setBackgroundColor(grey, 0.3f)).setBorder(Border.NO_BORDER));
        table2.addCell(new Cell(2,1).add(new Paragraph("\n").setBackgroundColor(grey, 0.3f)).setBorder(Border.NO_BORDER));
        //  ROW 05
        table2.addCell(new Cell(2,1).add(new Paragraph("\n").setBackgroundColor(grey, 0.3f)).setBorder(Border.NO_BORDER));
        table2.addCell(new Cell(2,1).add(new Paragraph("\n").setBackgroundColor(grey, 0.3f)).setBorder(Border.NO_BORDER));
        //  ROW 06
        table2.addCell(new Cell().add(new Paragraph("SUB TOTAL :").setBackgroundColor(grey, 0.3f).setBold()).setBorder(Border.NO_BORDER));
        table2.addCell(new Cell().add(new Paragraph(price).setBackgroundColor(grey, 0.3f).setBold()).setBorder(Border.NO_BORDER));

        //        TABLE 3

        float[] columnWidth3 = {280, 280};
        Table table3 = new Table(columnWidth3);

        //  ROW 01
        table3.addCell(new Cell().add(new Paragraph("PAYMENT MODE - CASH").setBold()).setBorder(Border.NO_BORDER));
        table3.addCell(new Cell().add(new Paragraph("CONTACT").setBold()).setBorder(Border.NO_BORDER));
        //  ROW 02
        table3.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table3.addCell(new Cell().add(new Paragraph("contact@tameerenautrust.com")).setBorder(Border.NO_BORDER));
        //  ROW 03
        table3.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table3.addCell(new Cell().add(new Paragraph("+91 9198809256")).setBorder(Border.NO_BORDER));
        //  ROW 04
        table3.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table3.addCell(new Cell().add(new Paragraph("Lucknow Uttar Pradesh")).setBorder(Border.NO_BORDER));
        //  ROW 05
        table3.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table3.addCell(new Cell().add(new Paragraph("Pin Code 226020")).setBorder(Border.NO_BORDER));

        document.add(table1);
        document.add(new Paragraph("\n"));
        document.add(table2);
        document.add(new Paragraph("\n"));
        document.add(table3);

        Toast.makeText(this, "Done!", Toast.LENGTH_SHORT).show();
        document.close();
    }
}