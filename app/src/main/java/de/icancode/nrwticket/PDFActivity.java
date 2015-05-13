package de.icancode.nrwticket;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.joanzapata.pdfview.PDFView;
import com.joanzapata.pdfview.listener.OnPageChangeListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.NonConfigurationInstance;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;


@EActivity(R.layout.activity_pdf)
@OptionsMenu(R.menu.menu_pdf)
public class PDFActivity extends Activity implements OnPageChangeListener {

    public static final String SAMPLE_FILE = "semesterticket.pdf";

    public static final String ABOUT_FILE = "about.pdf";

    static final String OFFSET_X = "xoffset";
    static final String OFFSET_Y = "yoffset ";
    static final String ZOOM = "ZOOM";

    private float mOffsetX;
    private float mOffsetY;
    private float mZoom;

    @ViewById
    PDFView pdfView;

    @NonConfigurationInstance
    String pdfName = SAMPLE_FILE;

    @NonConfigurationInstance
    Integer pageNumber = 1;

    @AfterViews
    void afterViews () {
        display(pdfName, false);
    }

    @OptionsItem
    public void about () {
        if (!displaying(ABOUT_FILE)) display(ABOUT_FILE, true);
    }

    @OptionsItem
    public void settings () {

    }

    @Override
    protected void onCreate (final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void onRestoreInstanceState (Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mOffsetX = savedInstanceState.getInt(OFFSET_X);
        mOffsetY = savedInstanceState.getInt(OFFSET_Y);
        mZoom = savedInstanceState.getInt(ZOOM);
        Log.i("PDF Position", "Setze alte Koordinaten");
        pdfView.moveTo(mOffsetX, mOffsetY);

    }

    @Override
    protected void onStop () {
        super.onStop();
        Log.i("PDF Position", pdfView.getCurrentXOffset() + "");
        Log.i("PDF Position", pdfView.getCurrentYOffset() + "");


    }

    @Override
    protected void onSaveInstanceState (final Bundle outState) {
        mOffsetX = pdfView.getCurrentXOffset();
        mOffsetY = pdfView.getCurrentYOffset();
        mZoom = pdfView.getZoom();
        outState.putFloat(OFFSET_X, mOffsetX);
        outState.putFloat(OFFSET_Y, mOffsetY);
        outState.putFloat(ZOOM, mZoom );
        super.onSaveInstanceState(outState);
    }

    private void display (String assetFileName, boolean jumpToFirstPage) {
        if (jumpToFirstPage) pageNumber = 1;
        setTitle(getString(R.string.app_name));

        pdfView.fromAsset(assetFileName).defaultPage(pageNumber).onPageChange(this).load();

    }

    /*
    PDF auswählen und richtig positionieren anschließen müsste man aus dem Bildschrimausschnitt und dem offset die anderen beiden Views berechnen können.
     */

    @Override
    public void onPageChanged (int page, int pageCount) {
        pageNumber = page;
        //setTitle(format("%s %s / %s", pdfName, page, pageCount));
    }

    @Override
    public void onBackPressed () {
        if (ABOUT_FILE.equals(pdfName)) {
            display(SAMPLE_FILE, true);
        } else {
            super.onBackPressed();
        }
    }

    private boolean displaying (String fileName) {
        return fileName.equals(pdfName);
    }
}
