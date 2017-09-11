package ca.six.ui.others.trianglify.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ca.six.ui.others.R;
import ca.six.ui.others.trianglify.ColorBrewer;
import ca.six.ui.others.trianglify.TrianglifyView;


public class TrianglifySampleActivity extends AppCompatActivity {

    TrianglifyView trianglifyView;
    SeekBar cellSizeControl;
    SeekBar varianceControl;
    Spinner colorControl;
    Button saveToGallery;

    private int cellSize, variance;

    private ImageExporter exporter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trianglify_sample);

        trianglifyView = (TrianglifyView) findViewById(R.id.trianglify);
        cellSizeControl = (SeekBar) findViewById(R.id.cellSizeControl);
        varianceControl = (SeekBar) findViewById(R.id.varianceControl);
        colorControl = (Spinner) findViewById(R.id.colorControl);
        saveToGallery = (Button) findViewById(R.id.saveToGalleryButton);

        initCellSizeControl();
        initVarianceControl();
        initColorControl();

        trianglifyView.setDrawingCacheEnabled(true);
        exporter = new ImageExporter();
    }

    public void genTrianglify(View v){
        trianglifyView.setDrawingCacheEnabled(false);
        trianglifyView.setCellSize(cellSize);
        trianglifyView.setVariance(variance);
        trianglifyView.setDrawingCacheEnabled(true);
    }

    public void saveToGallery(View v) {
        exportViewToImage();
    }

    private void exportViewToImage() {
        try {
            exporter.exportFromView(this, trianglifyView);
            Toast.makeText(this, "Image generated succesfully", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "EXPORTING FAILED", Toast.LENGTH_SHORT).show();
        }
    }

    private void initCellSizeControl() {
        cellSizeControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cellSize = (progress == 0) ? 200 : progress * 10;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void initVarianceControl() {
        varianceControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                variance = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void initColorControl() {
        final List<String> list = new ArrayList<>(ColorBrewer.values().length);
        final ColorBrewer[] colors = ColorBrewer.values();
        for (ColorBrewer color : colors) {
            list.add(color.name());
        }

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        colorControl.setAdapter(adapter);

        colorControl.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                trianglifyView.setDrawingCacheEnabled(false);
                trianglifyView.setColor(colors[position]);
                trianglifyView.setDrawingCacheEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
