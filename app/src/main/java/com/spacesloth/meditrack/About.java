/* (C) 2022 */
package com.spacesloth.meditrack;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class About extends AppCompatActivity {

//    final Toasts toasts = new Toasts(this);

    Button btnBack;
    TextView meditrackParagraph;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentViewBasedOnThemeSetting();
        findViewsByIds();
        initiateTextViews();
        setButtonOnClickListeners();
    }

    private void setContentViewBasedOnThemeSetting() {
        setTheme(R.style.MeditrackAppTheme_BlackBackground);
        setContentView(R.layout.app_about);
    }

    private void findViewsByIds() {
        meditrackParagraph = findViewById(R.id.meditrack_paragraph);
        btnBack = findViewById(R.id.btn_back);
    }

    private void initiateTextViews() {}
//
//    private void copyCryptoAddressToClipboard(int cryptoNumber) {
//        ClipboardManager clipboardManager =
//                (ClipboardManager) this.getSystemService((Context.CLIPBOARD_SERVICE));
//        ClipData clipData = null;
//
//        String toastMessage = switch (cryptoNumber) {
//            case BTC -> {
//                clipData =
//                        ClipData.newPlainText(
//                                "btcAddress", this.getString(R.string.bitcoin_address));
//                yield this.getString(R.string.btc_address_copied);
//            }
//            case XMR -> {
//                clipData =
//                        ClipData.newPlainText(
//                                "xmrAddress", this.getString(R.string.monero_address));
//                yield this.getString(R.string.xmr_address_copied);
//            }
//            default -> "";
//        };
//
//        assert clipData != null;
//        clipboardManager.setPrimaryClip(clipData);
//        toasts.showCustomToast(toastMessage);
//    }

    private void setButtonOnClickListeners() {
        btnBack.setOnClickListener(v -> finish());
    }
}
