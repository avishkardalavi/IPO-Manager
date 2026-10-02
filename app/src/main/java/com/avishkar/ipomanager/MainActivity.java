package com.avishkar.ipomanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.*;

import com.avishkar.ipomanager.data.*;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

import android.os.Build;
import android.view.WindowInsets;
import android.graphics.Insets;

import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends Activity {

    LinearLayout content;
    AppDatabase db;

    int selectedNav = 0;

    private static final int REQUEST_EXPORT_DATA = 1001;
    private static final int REQUEST_IMPORT_DATA = 1002;

    // ---------------------------------------------------------
    // PHASE 6 COLORS
    // ---------------------------------------------------------

    final int BG = Color.rgb(246, 248, 252);
    final int CARD = Color.WHITE;
    final int TEXT = Color.rgb(25, 31, 45);
    final int MUTED = Color.rgb(105, 114, 130);
    final int PRIMARY = Color.rgb(35, 91, 219);
    final int GREEN = Color.rgb(25, 150, 90);
    final int RED = Color.rgb(210, 65, 65);
    final int BORDER = Color.rgb(225, 229, 237);

    // ---------------------------------------------------------
    // ACTIVITY
    // ---------------------------------------------------------

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);

        db = AppDatabase.getInstance(this);

        Executors.newSingleThreadExecutor().execute(() -> {

            seedDataIfNeeded();

            runOnUiThread(this::home);
        });
    }

    @Override
    protected void onActivityResult(
            int requestCode,
            int resultCode,
            Intent data
    ) {

        super.onActivityResult(
                requestCode,
                resultCode,
                data
        );

        if (
                resultCode != RESULT_OK ||
                        data == null ||
                        data.getData() == null
        ) {
            return;
        }

        Uri uri =
                data.getData();

        if (
                requestCode ==
                        REQUEST_EXPORT_DATA
        ) {

            performExport(uri);

        }
        else if (
                requestCode ==
                        REQUEST_IMPORT_DATA
        ) {

            performImport(uri);
        }
    }

    // ---------------------------------------------------------
    // BASIC HELPERS
    // ---------------------------------------------------------

    int dp(int n) {
        return (int) (
                n * getResources().getDisplayMetrics().density + 0.5f
        );
    }

    String money(long n) {
        return NumberFormat
                .getNumberInstance(new Locale("en", "IN"))
                .format(n);
    }

    TextView tv(
            String text,
            float size,
            boolean bold
    ) {

        TextView t = new TextView(this);

        t.setText(text);
        t.setTextSize(size);
        t.setTextColor(TEXT);

        if (bold) {
            t.setTypeface(
                    Typeface.DEFAULT,
                    Typeface.BOLD
            );
        }

        t.setGravity(Gravity.CENTER_VERTICAL);

        return t;
    }

    GradientDrawable roundedBackground(
            int color,
            int strokeColor
    ) {

        GradientDrawable drawable =
                new GradientDrawable();

        drawable.setColor(color);

        drawable.setCornerRadius(
                dp(16)
        );

        if (strokeColor != 0) {

            drawable.setStroke(
                    dp(1),
                    strokeColor
            );
        }

        return drawable;
    }

    TextView sectionTitle(String text) {

        TextView t =
                tv(text, 19, true);

        t.setTextColor(TEXT);

        t.setPadding(
                0,
                dp(14),
                0,
                dp(8)
        );

        return t;
    }

    TextView label(String text) {

        TextView t =
                tv(
                        text.toUpperCase(
                                Locale.getDefault()
                        ),
                        11,
                        true
                );

        t.setTextColor(MUTED);

        t.setPadding(
                0,
                dp(4),
                0,
                dp(6)
        );

        return t;
    }

    void toast(String text) {

        Toast.makeText(
                this,
                text,
                Toast.LENGTH_SHORT
        ).show();
    }

    // ---------------------------------------------------------
    // SAMPLE DATA
    // ---------------------------------------------------------

    void seedDataIfNeeded() {

        if (db.profitDao().count() == 0) {

            String[][] a = {
            };

            for (String[] x : a) {

                db.profitDao().insert(
                        new ProfitEntity(
                                x[0],
                                Long.parseLong(x[1]),
                                "PROFIT",
                                x[2],
                                x[3],
                                x[4],
                                Integer.parseInt(x[4])
                        )
                );
            }
        }

        if (db.transferDao().count() == 0) {

            String[][] a = {
            };

            for (String[] x : a) {

                db.transferDao().insert(
                        new TransferEntity(
                                Long.parseLong(x[0]),
                                x[1],
                                x[2],
                                x[3],
                                x[4],
                                Boolean.parseBoolean(x[5])
                        )
                );
            }
        }
    }

    // ---------------------------------------------------------
    // BASE UI
    // ---------------------------------------------------------

    void base() {

        LinearLayout root =
                new LinearLayout(this);

        root.setOrientation(
                LinearLayout.VERTICAL
        );

        root.setBackgroundColor(
                Color.rgb(246, 248, 252)
        );

        // -------------------------------------------------
        // CONTENT
        // -------------------------------------------------

        content =
                new LinearLayout(this);

        content.setOrientation(
                LinearLayout.VERTICAL
        );

        content.setPadding(
                dp(18),
                dp(18),
                dp(18),
                dp(18)
        );

        ScrollView scroll =
                new ScrollView(this);

        scroll.setFillViewport(true);

        // Important:
        // Gives the scrollable content enough space so
        // the last card/button never hides behind navigation.
        content.setPadding(
                dp(18),
                dp(18),
                dp(18),
                dp(90)
        );

        scroll.addView(content);

        root.addView(
                scroll,
                new LinearLayout.LayoutParams(
                        -1,
                        0,
                        1
                )
        );

        // -------------------------------------------------
        // GLASS NAVIGATION
        // -------------------------------------------------

        FrameLayout navHolder =
                new FrameLayout(this);

        navHolder.setPadding(
                dp(10),
                dp(5),
                dp(10),
                dp(8)
        );

        LinearLayout nav =
                new LinearLayout(this);

        nav.setOrientation(
                LinearLayout.HORIZONTAL
        );

        nav.setGravity(
                Gravity.CENTER
        );

        nav.setPadding(
                dp(6),
                dp(6),
                dp(6),
                dp(6)
        );

        GradientDrawable glass =
                new GradientDrawable();

        glass.setColor(
                Color.argb(
                        235,
                        255,
                        255,
                        255
                )
        );

        glass.setCornerRadius(
                dp(24)
        );

        glass.setStroke(
                dp(1),
                Color.argb(
                        150,
                        220,
                        226,
                        236
                )
        );

        nav.setBackground(glass);

        nav.setElevation(
                dp(10)
        );

        FrameLayout.LayoutParams navParams =
                new FrameLayout.LayoutParams(
                        -1,
                        dp(64)
                );

        navParams.gravity =
                Gravity.CENTER;

        navHolder.addView(
                nav,
                navParams
        );

        // -------------------------------------------------
        // NAV ITEMS
        // -------------------------------------------------

        nav.addView(
                navButton(
                        "⌂",
                        "Home",
                        0
                )
        );

        nav.addView(
                navButton(
                        "₹",
                        "Profits",
                        1
                )
        );

        nav.addView(
                navButton(
                        "⇄",
                        "Transfers",
                        2
                )
        );

        nav.addView(
                navButton(
                        "⚙",
                        "Settings",
                        3
                )
        );

        // -------------------------------------------------
        // ROOT NAV
        // -------------------------------------------------

        root.addView(
                navHolder,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(78)
                )
        );

        // -------------------------------------------------
        // SYSTEM BAR / PHONE SCREEN INSETS
        // -------------------------------------------------

        root.setOnApplyWindowInsetsListener(
                (v, insets) -> {

                    int topInset = 0;
                    int bottomInset = 0;

                    if (Build.VERSION.SDK_INT >= 30) {

                        Insets systemBars =
                                insets.getInsets(
                                        WindowInsets.Type.systemBars()
                                );

                        topInset =
                                systemBars.top;

                        bottomInset =
                                systemBars.bottom;

                    } else {

                        topInset =
                                insets.getSystemWindowInsetTop();

                        bottomInset =
                                insets.getSystemWindowInsetBottom();
                    }

                    // Keep application content below
                    // the phone status bar.
                    root.setPadding(
                            0,
                            topInset,
                            0,
                            0
                    );

                    // Keep glass navigation above the
                    // Android navigation / gesture area.
                    navHolder.setPadding(
                            dp(10),
                            dp(5),
                            dp(10),
                            dp(8) + bottomInset
                    );

                    return insets;
                }
        );

        setContentView(root);

        // Request system insets
        root.requestApplyInsets();
    }

    LinearLayout navButton(
            String icon,
            String title,
            int position
    ) {

        LinearLayout item =
                new LinearLayout(this);

        item.setOrientation(
                LinearLayout.VERTICAL
        );

        item.setGravity(
                Gravity.CENTER
        );

        item.setPadding(
                dp(10),
                dp(5),
                dp(10),
                dp(5)
        );

        LinearLayout.LayoutParams itemParams =
                new LinearLayout.LayoutParams(
                        0,
                        -1,
                        1
                );

        itemParams.setMargins(
                dp(3),
                0,
                dp(3),
                0
        );

        item.setLayoutParams(itemParams);

        // -------------------------------------------------
        // ICON
        // -------------------------------------------------

        TextView iconView =
                new TextView(this);

        iconView.setText(icon);

        iconView.setGravity(
                Gravity.CENTER
        );

        iconView.setTextSize(18);

        // -------------------------------------------------
        // LABEL
        // -------------------------------------------------

        TextView labelView =
                new TextView(this);

        labelView.setText(title);

        labelView.setGravity(
                Gravity.CENTER
        );

        labelView.setTextSize(10);

        // -------------------------------------------------
        // ACTIVE / INACTIVE STATE
        // -------------------------------------------------

        if (selectedNav == position) {

            GradientDrawable activeBg =
                    new GradientDrawable();

            activeBg.setColor(
                    Color.argb(
                            35,
                            35,
                            91,
                            219
                    )
            );

            activeBg.setCornerRadius(
                    dp(17)
            );

            item.setBackground(
                    activeBg
            );

            iconView.setTextColor(
                    Color.rgb(
                            35,
                            91,
                            219
                    )
            );

            labelView.setTextColor(
                    Color.rgb(
                            35,
                            91,
                            219
                    )
            );

            iconView.setTypeface(
                    Typeface.DEFAULT,
                    Typeface.BOLD
            );

            labelView.setTypeface(
                    Typeface.DEFAULT,
                    Typeface.BOLD
            );

        } else {

            // No background for inactive items
            item.setBackgroundColor(
                    Color.TRANSPARENT
            );

            iconView.setTextColor(
                    Color.rgb(
                            125,
                            133,
                            148
                    )
            );

            labelView.setTextColor(
                    Color.rgb(
                            125,
                            133,
                            148
                    )
            );

            iconView.setTypeface(
                    Typeface.DEFAULT,
                    Typeface.NORMAL
            );

            labelView.setTypeface(
                    Typeface.DEFAULT,
                    Typeface.NORMAL
            );
        }

        // -------------------------------------------------
        // ADD VIEWS
        // -------------------------------------------------

        item.addView(
                iconView,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(27)
                )
        );

        item.addView(
                labelView,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(20)
                )
        );

        // -------------------------------------------------
        // CLICK
        // -------------------------------------------------

        item.setOnClickListener(
                v -> {

                    if (selectedNav == position) {
                        return;
                    }

                    selectedNav = position;

                    if (position == 0) {

                        home();

                    } else if (position == 1) {

                        profits();

                    } else if (position == 2) {

                        transfers();

                    } else {

                        settings();
                    }
                }
        );

        return item;
    }

    // ---------------------------------------------------------
    // HEADER
    // ---------------------------------------------------------

    void head(
            String title,
            String subtitle
    ) {

        content.removeAllViews();

        TextView titleView =
                tv(
                        title,
                        28,
                        true
                );

        titleView.setTextColor(
                TEXT
        );

        titleView.setPadding(
                0,
                dp(4),
                0,
                dp(2)
        );

        content.addView(
                titleView
        );

        if (!subtitle.isEmpty()) {

            TextView sub =
                    tv(
                            subtitle,
                            13,
                            false
                    );

            sub.setTextColor(
                    MUTED
            );

            sub.setPadding(
                    0,
                    0,
                    0,
                    dp(10)
            );

            content.addView(
                    sub
            );
        }
    }

    // ---------------------------------------------------------
    // CARD
    // ---------------------------------------------------------

    TextView card(
            String text
    ) {

        TextView t =
                tv(
                        text,
                        15,
                        false
                );

        t.setTextColor(
                TEXT
        );

        t.setPadding(
                dp(18),
                dp(16),
                dp(18),
                dp(16)
        );

        t.setBackground(
                roundedBackground(
                        CARD,
                        BORDER
                )
        );

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(
                        -1,
                        -2
                );

        p.setMargins(
                0,
                dp(6),
                0,
                dp(6)
        );

        t.setLayoutParams(p);

        return t;
    }

    // ---------------------------------------------------------
    // PANEL
    // ---------------------------------------------------------

    LinearLayout panel() {

        LinearLayout p =
                new LinearLayout(this);

        p.setOrientation(
                LinearLayout.VERTICAL
        );

        p.setPadding(
                dp(16),
                dp(15),
                dp(16),
                dp(15)
        );

        p.setBackground(
                roundedBackground(
                        CARD,
                        BORDER
                )
        );

        LinearLayout.LayoutParams lp =
                new LinearLayout.LayoutParams(
                        -1,
                        -2
                );

        lp.setMargins(
                0,
                dp(5),
                0,
                dp(7)
        );

        p.setLayoutParams(lp);

        return p;
    }

    void addPanel(
            LinearLayout panel
    ) {

        content.addView(
                panel
        );
    }

    // ---------------------------------------------------------
    // STAT CARD
    // ---------------------------------------------------------

    TextView statCard(
            String title,
            String value,
            int color
    ) {

        TextView t =
                tv(
                        title +
                                "\n" +
                                value,
                        15,
                        true
                );

        t.setTextColor(
                color
        );

        t.setPadding(
                dp(16),
                dp(14),
                dp(16),
                dp(14)
        );

        t.setBackground(
                roundedBackground(
                        CARD,
                        BORDER
                )
        );

        return t;
    }

    // ---------------------------------------------------------
    // BUTTON
    // ---------------------------------------------------------

    void button(
            String text,
            View.OnClickListener listener
    ) {

        Button b =
                new Button(this);

        b.setText(text);

        b.setAllCaps(false);

        b.setTextSize(14);

        b.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        b.setTextColor(
                Color.WHITE
        );

        b.setGravity(
                Gravity.CENTER
        );

        b.setMinHeight(
                dp(50)
        );

        GradientDrawable background =
                new GradientDrawable();

        background.setColor(
                PRIMARY
        );

        background.setCornerRadius(
                dp(14)
        );

        b.setBackground(
                background
        );

        b.setOnClickListener(
                listener
        );

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(
                        -1,
                        dp(50)
                );

        p.setMargins(
                0,
                dp(6),
                0,
                dp(6)
        );

        content.addView(
                b,
                p
        );
    }

    void deleteButton(
            String text,
            View.OnClickListener listener
    ) {

        Button b =
                new Button(this);

        b.setText(text);
        b.setAllCaps(false);
        b.setTextSize(14);
        b.setTextColor(Color.WHITE);

        GradientDrawable redBackground =
                new GradientDrawable();

        redBackground.setColor(
                Color.rgb(220, 53, 69)
        );

        redBackground.setCornerRadius(
                dp(14)
        );

        b.setBackground(redBackground);

        b.setOnClickListener(listener);

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        -1,
                        dp(52)
                );

        params.setMargins(
                0,
                dp(6),
                0,
                dp(6)
        );

        b.setLayoutParams(params);

        content.addView(b);
    }

    // ---------------------------------------------------------
    // SECONDARY BUTTON
    // ---------------------------------------------------------

    void secondaryButton(
            String text,
            View.OnClickListener listener
    ) {

        Button b =
                new Button(this);

        b.setText(text);

        b.setAllCaps(false);

        b.setTextSize(14);

        b.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        b.setTextColor(
                PRIMARY
        );

        b.setMinHeight(
                dp(48)
        );

        b.setBackground(
                roundedBackground(
                        CARD,
                        PRIMARY
                )
        );

        b.setOnClickListener(
                listener
        );

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(
                        -1,
                        dp(48)
                );

        p.setMargins(
                0,
                dp(5),
                0,
                dp(5)
        );

        content.addView(
                b,
                p
        );
    }

    // ---------------------------------------------------------
    // HOME
    // ---------------------------------------------------------

    void home() {

        selectedNav = 0;
        base();

        head(
                "IPO Manager",
                "Your personal offline IPO tracker"
        );

        Executors
                .newSingleThreadExecutor()
                .execute(() -> {

                    List<ProfitEntity> profitRows =
                            db.profitDao().getAll();

                    List<TransferEntity> transferRows =
                            db.transferDao().getAll();

                    long profit = 0;
                    long loss = 0;
                    long transferTotal = 0;

                    for (ProfitEntity x :
                            profitRows) {

                        if ("LOSS".equals(x.type)) {

                            loss += x.amount;

                        } else {

                            profit += x.amount;
                        }
                    }

                    for (TransferEntity x :
                            transferRows) {

                        transferTotal +=
                                x.amount;
                    }

                    final long finalProfit =
                            profit;

                    final long finalLoss =
                            loss;

                    final long finalTransfers =
                            transferTotal;

                    final long finalNet =
                            profit - loss;

                    final int profitCount =
                            profitRows.size();

                    final int transferCount =
                            transferRows.size();

                    runOnUiThread(() -> {

                        // NET PROFIT

                        LinearLayout hero =
                                panel();

                        TextView small =
                                tv(
                                        "CURRENT NET PROFIT",
                                        11,
                                        true
                                );

                        small.setTextColor(
                                MUTED
                        );

                        hero.addView(
                                small
                        );

                        TextView net =
                                tv(
                                        "₹" +
                                                money(finalNet),
                                        31,
                                        true
                                );

                        net.setTextColor(
                                finalNet >= 0
                                        ? GREEN
                                        : RED
                        );

                        net.setPadding(
                                0,
                                dp(4),
                                0,
                                dp(3)
                        );

                        hero.addView(
                                net
                        );

                        TextView info =
                                tv(
                                        profitCount +
                                                " IPO results  •  " +
                                                transferCount +
                                                " fund transfers",
                                        12,
                                        false
                                );

                        info.setTextColor(
                                MUTED
                        );

                        hero.addView(
                                info
                        );

                        addPanel(hero);

                        // PROFIT / LOSS ROW

                        LinearLayout row =
                                new LinearLayout(this);

                        row.setOrientation(
                                LinearLayout.HORIZONTAL
                        );

                        TextView p =
                                statCard(
                                        "IPO PROFITS",
                                        "₹" +
                                                money(
                                                        finalProfit
                                                ),
                                        GREEN
                                );

                        TextView l =
                                statCard(
                                        "TOTAL LOSS",
                                        "₹" +
                                                money(
                                                        finalLoss
                                                ),
                                        RED
                                );

                        LinearLayout.LayoutParams p1 =
                                new LinearLayout.LayoutParams(
                                        0,
                                        -2,
                                        1
                                );

                        p1.setMargins(
                                0,
                                dp(4),
                                dp(5),
                                dp(4)
                        );

                        row.addView(
                                p,
                                p1
                        );

                        LinearLayout.LayoutParams p2 =
                                new LinearLayout.LayoutParams(
                                        0,
                                        -2,
                                        1
                                );

                        p2.setMargins(
                                dp(5),
                                dp(4),
                                0,
                                dp(4)
                        );

                        row.addView(
                                l,
                                p2
                        );

                        content.addView(
                                row
                        );

                        // TRANSFERS

                        TextView transfer =
                                card(
                                        "FUND TRANSFERS\n" +
                                                "₹" +
                                                money(
                                                        finalTransfers
                                                ) +
                                                "\n\n" +
                                                transferCount +
                                                " transactions"
                                );

                        transfer.setOnClickListener(
                                v -> transfers()
                        );

                        content.addView(
                                transfer
                        );

                        content.addView(
                                sectionTitle(
                                        "Quick Actions"
                                )
                        );

                        button(
                                "＋  Add IPO Result",
                                v -> addProfit()
                        );

                        button(
                                "＋  Add Fund Transfer",
                                v -> addTransfer()
                        );

                        secondaryButton(
                                "📊  Open Analytics",
                                v -> {

                                    Intent intent =
                                            new Intent(
                                                    MainActivity.this,
                                                    AnalyticsActivity.class
                                            );

                                    startActivity(
                                            intent
                                    );
                                }
                        );
                    });
                });
    }

    // ---------------------------------------------------------
    // IPO PROFITS
    // ---------------------------------------------------------

    void profits() {

        selectedNav = 1;

        base();

        head(
                "IPO Profits",
                "Track all your IPO results"
        );

        EditText search =
                field(
                        "Search IPO name..."
                );

        search.setSingleLine(true);

        content.addView(
                label("YEAR FILTER")
        );

        Spinner yearSpinner =
                new Spinner(this);

        String[] years = {
                "All Years",
                "2026",
                "2025"
        };

        yearSpinner.setAdapter(
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_dropdown_item,
                        years
                )
        );

        content.addView(
                yearSpinner
        );

        LinearLayout listContainer =
                new LinearLayout(this);

        listContainer.setOrientation(
                LinearLayout.VERTICAL
        );

        content.addView(
                listContainer
        );

        button(
                "＋  Add IPO Result",
                v -> addProfit()
        );

        Executors
                .newSingleThreadExecutor()
                .execute(() -> {

                    List<ProfitEntity> rows =
                            db.profitDao().getAll();

                    runOnUiThread(() -> {

                        Runnable refresh =
                                () -> {

                                    listContainer
                                            .removeAllViews();

                                    long totalProfit =
                                            0;

                                    long totalLoss =
                                            0;

                                    int count =
                                            0;

                                    String query =
                                            search.getText()
                                                    .toString()
                                                    .trim()
                                                    .toLowerCase(
                                                            Locale.getDefault()
                                                    );

                                    String selectedYear =
                                            yearSpinner
                                                    .getSelectedItem()
                                                    .toString();

                                    for (
                                            ProfitEntity x :
                                            rows
                                    ) {

                                        boolean nameMatch =
                                                x.ipoName
                                                        .toLowerCase(
                                                                Locale.getDefault()
                                                        )
                                                        .contains(
                                                                query
                                                        );

                                        boolean yearMatch =
                                                selectedYear
                                                        .equals(
                                                                "All Years"
                                                        )
                                                        ||
                                                        String.valueOf(
                                                                        x.year
                                                                )
                                                                .equals(
                                                                        selectedYear
                                                                );

                                        if (
                                                !nameMatch
                                                        ||
                                                        !yearMatch
                                        ) {
                                            continue;
                                        }

                                        count++;

                                        if (
                                                "LOSS"
                                                        .equals(
                                                                x.type
                                                        )
                                        ) {

                                            totalLoss +=
                                                    x.amount;

                                        } else {

                                            totalProfit +=
                                                    x.amount;
                                        }
                                    }

                                    // SUMMARY

                                    LinearLayout summary =
                                            panel();

                                    TextView summaryTitle =
                                            tv(
                                                    "FILTERED SUMMARY",
                                                    11,
                                                    true
                                            );

                                    summaryTitle
                                            .setTextColor(
                                                    MUTED
                                            );

                                    summary.addView(
                                            summaryTitle
                                    );

                                    TextView summaryValue =
                                            tv(
                                                    "Profit  ₹" +
                                                            money(
                                                                    totalProfit
                                                            ) +
                                                            "     Loss  ₹" +
                                                            money(
                                                                    totalLoss
                                                            ) +
                                                            "\n" +
                                                            "Net Profit  ₹" +
                                                            money(
                                                                    totalProfit -
                                                                            totalLoss
                                                            ) +
                                                            "     Records  " +
                                                            count,
                                                    14,
                                                    true
                                            );

                                    summaryValue
                                            .setTextColor(
                                                    totalProfit -
                                                            totalLoss >= 0
                                                            ? GREEN
                                                            : RED
                                            );

                                    summary.addView(
                                            summaryValue
                                    );

                                    listContainer.addView(
                                            summary
                                    );

                                    if (
                                            count == 0
                                    ) {

                                        listContainer
                                                .addView(
                                                        card(
                                                                "No IPO records found."
                                                        )
                                                );

                                        return;
                                    }

                                    // RECORDS

                                    for (
                                            ProfitEntity x :
                                            rows
                                    ) {

                                        boolean nameMatch =
                                                x.ipoName
                                                        .toLowerCase(
                                                                Locale.getDefault()
                                                        )
                                                        .contains(
                                                                query
                                                        );

                                        boolean yearMatch =
                                                selectedYear
                                                        .equals(
                                                                "All Years"
                                                        )
                                                        ||
                                                        String.valueOf(
                                                                        x.year
                                                                )
                                                                .equals(
                                                                        selectedYear
                                                                );

                                        if (
                                                !nameMatch
                                                        ||
                                                        !yearMatch
                                        ) {
                                            continue;
                                        }

                                        LinearLayout record =
                                                panel();

                                        LinearLayout top =
                                                new LinearLayout(
                                                        this
                                                );

                                        top.setOrientation(
                                                LinearLayout.HORIZONTAL
                                        );

                                        TextView name =
                                                tv(
                                                        x.ipoName,
                                                        17,
                                                        true
                                                );

                                        TextView amountView =
                                                tv(
                                                        (
                                                                "LOSS".equals(
                                                                        x.type
                                                                )
                                                                        ? "- ₹"
                                                                        : "+ ₹"
                                                        ) +
                                                                money(
                                                                        x.amount
                                                                ),
                                                        17,
                                                        true
                                                );

                                        amountView
                                                .setTextColor(
                                                        "LOSS".equals(
                                                                x.type
                                                        )
                                                                ? RED
                                                                : GREEN
                                                );

                                        amountView
                                                .setGravity(
                                                        Gravity.END
                                                );

                                        top.addView(
                                                name,
                                                new LinearLayout.LayoutParams(
                                                        0,
                                                        -2,
                                                        1
                                                )
                                        );

                                        top.addView(
                                                amountView
                                        );

                                        record.addView(
                                                top
                                        );

                                        TextView details =
                                                tv(
                                                        x.bank +
                                                                "  •  " +
                                                                x.person +
                                                                "\n" +
                                                                x.date +
                                                                "\n" +
                                                                (
                                                                        "LOSS"
                                                                                .equals(
                                                                                        x.type
                                                                                )
                                                                                ? "LOSS"
                                                                                : "PROFIT"
                                                                ) +
                                                                "  •  Tap to edit or delete",
                                                        12,
                                                        false
                                                );

                                        details.setTextColor(
                                                MUTED
                                        );

                                        details.setPadding(
                                                0,
                                                dp(7),
                                                0,
                                                0
                                        );

                                        record.addView(
                                                details
                                        );

                                        record.setOnClickListener(
                                                v -> editProfit(x)
                                        );

                                        listContainer
                                                .addView(
                                                        record
                                                );
                                    }
                                };

                        addTextWatcher(
                                search,
                                refresh
                        );

                        yearSpinner
                                .setOnItemSelectedListener(
                                        new AdapterView
                                                .OnItemSelectedListener() {

                                            @Override
                                            public void onItemSelected(
                                                    AdapterView<?> parent,
                                                    View view,
                                                    int position,
                                                    long id
                                            ) {

                                                refresh.run();
                                            }

                                            @Override
                                            public void onNothingSelected(
                                                    AdapterView<?> parent
                                            ) {
                                            }
                                        }
                                );

                        refresh.run();
                    });
                });
    }

    // ---------------------------------------------------------
    // EDIT IPO
    // ---------------------------------------------------------

    void editProfit(
            ProfitEntity old
    ) {

        base();

        head(
                "Edit IPO Result",
                "Update the selected IPO result"
        );

        EditText name =
                field("IPO Name");

        name.setText(
                old.ipoName
        );

        EditText amount =
                field("Amount");

        amount.setInputType(
                InputType.TYPE_CLASS_NUMBER
        );

        amount.setText(
                String.valueOf(
                        old.amount
                )
        );

        EditText bank =
                field(
                        "Bank / Account"
                );

        bank.setText(
                old.bank
        );

        EditText person =
                field(
                        "Received By"
                );

        person.setText(
                old.person
        );

        EditText date =
                field("Date");

        date.setText(
                old.date
        );

        prepareDateField(
                date
        );

        content.addView(
                label(
                        "RESULT TYPE"
                )
        );

        Spinner type =
                new Spinner(this);

        type.setAdapter(
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_dropdown_item,
                        new String[]{
                                "PROFIT",
                                "LOSS"
                        }
                )
        );

        type.setSelection(
                "LOSS".equals(
                        old.type
                )
                        ? 1
                        : 0
        );

        content.addView(
                type
        );

        button(
                "SAVE CHANGES",
                v -> {

                    String newName =
                            name.getText()
                                    .toString()
                                    .trim();

                    String amountText =
                            amount.getText()
                                    .toString()
                                    .trim();

                    String newBank =
                            bank.getText()
                                    .toString()
                                    .trim();

                    String newPerson =
                            person.getText()
                                    .toString()
                                    .trim();

                    String newDate =
                            date.getText()
                                    .toString()
                                    .trim();

                    if (
                            newName.isEmpty()
                                    ||
                                    amountText.isEmpty()
                                    ||
                                    newDate.isEmpty()
                    ) {

                        toast(
                                "Please fill the required fields."
                        );

                        return;
                    }

                    long newAmount;

                    try {

                        newAmount =
                                Long.parseLong(
                                        amountText
                                );

                        if (
                                newAmount <= 0
                        ) {
                            throw new Exception();
                        }

                    } catch (Exception e) {

                        toast(
                                "Enter a valid amount."
                        );

                        return;
                    }

                    String newType =
                            type.getSelectedItem()
                                    .toString();

                    int newYear =
                            extractYear(
                                    newDate
                            );

                    Executors
                            .newSingleThreadExecutor()
                            .execute(() -> {

                                db.profitDao()
                                        .updateProfit(

                                                old.ipoName,
                                                old.amount,
                                                old.type,
                                                old.bank,
                                                old.person,
                                                old.date,
                                                old.year,

                                                newName,
                                                newAmount,
                                                newType,
                                                newBank,
                                                newPerson,
                                                newDate,
                                                newYear
                                        );

                                runOnUiThread(() -> {

                                    toast(
                                            "IPO result updated."
                                    );

                                    profits();
                                });
                            });
                }
        );

        deleteButton(
                "DELETE RESULT",
                v -> confirmDeleteProfit(old)
        );

        secondaryButton(
                "Cancel",
                v -> profits()
        );
    }

    // ---------------------------------------------------------
    // DELETE IPO
    // ---------------------------------------------------------

    void confirmDeleteProfit(
            ProfitEntity item
    ) {

        new AlertDialog.Builder(this)

                .setTitle(
                        "Delete IPO Result?"
                )

                .setMessage(
                        "Are you sure you want to delete \"" +
                                item.ipoName +
                                "\"?"
                )

                .setNegativeButton(
                        "Cancel",
                        null
                )

                .setPositiveButton(
                        "Delete",
                        (dialog, which) -> {

                            Executors
                                    .newSingleThreadExecutor()
                                    .execute(() -> {

                                        db.profitDao()
                                                .deleteProfit(
                                                        item.ipoName,
                                                        item.amount,
                                                        item.type,
                                                        item.bank,
                                                        item.person,
                                                        item.date,
                                                        item.year
                                                );

                                        runOnUiThread(() -> {

                                            toast(
                                                    "IPO result deleted."
                                            );

                                            profits();
                                        });
                                    });
                        }
                )

                .show();
    }

    // ---------------------------------------------------------
    // FUND TRANSFERS
    // ---------------------------------------------------------

    void transfers() {

        selectedNav = 2;

        base();

        head(
                "Fund Transfers",
                "Track money moved between accounts"
        );

        EditText search =
                field(
                        "Search account or IPO..."
                );

        LinearLayout listContainer =
                new LinearLayout(this);

        listContainer.setOrientation(
                LinearLayout.VERTICAL
        );

        content.addView(
                listContainer
        );

        button(
                "＋  Add Fund Transfer",
                v -> addTransfer()
        );

        Executors
                .newSingleThreadExecutor()
                .execute(() -> {

                    List<TransferEntity> rows =
                            db.transferDao().getAll();

                    runOnUiThread(() -> {

                        Runnable refresh =
                                () -> {

                                    listContainer
                                            .removeAllViews();

                                    long total = 0;

                                    int count = 0;

                                    String query =
                                            search.getText()
                                                    .toString()
                                                    .trim()
                                                    .toLowerCase(
                                                            Locale.getDefault()
                                                    );

                                    for (
                                            TransferEntity x :
                                            rows
                                    ) {

                                        String combined =
                                                (
                                                        x.fromAccount +
                                                                " " +
                                                                x.toAccount +
                                                                " " +
                                                                x.ipoName
                                                )
                                                        .toLowerCase(
                                                                Locale.getDefault()
                                                        );

                                        if (
                                                !combined.contains(
                                                        query
                                                )
                                        ) {
                                            continue;
                                        }

                                        total +=
                                                x.amount;

                                        count++;
                                    }

                                    LinearLayout summary =
                                            panel();

                                    TextView title =
                                            tv(
                                                    "FILTERED SUMMARY",
                                                    11,
                                                    true
                                            );

                                    title.setTextColor(
                                            MUTED
                                    );

                                    summary.addView(
                                            title
                                    );

                                    TextView value =
                                            tv(
                                                    "₹" +
                                                            money(total) +
                                                            " transferred  •  " +
                                                            count +
                                                            " transactions",
                                                    14,
                                                    true
                                            );

                                    value.setTextColor(
                                            PRIMARY
                                    );

                                    summary.addView(
                                            value
                                    );

                                    listContainer
                                            .addView(
                                                    summary
                                            );

                                    if (
                                            count == 0
                                    ) {

                                        listContainer
                                                .addView(
                                                        card(
                                                                "No fund transfers found."
                                                        )
                                                );

                                        return;
                                    }

                                    for (
                                            TransferEntity x :
                                            rows
                                    ) {

                                        String combined =
                                                (
                                                        x.fromAccount +
                                                                " " +
                                                                x.toAccount +
                                                                " " +
                                                                x.ipoName
                                                )
                                                        .toLowerCase(
                                                                Locale.getDefault()
                                                        );

                                        if (
                                                !combined.contains(
                                                        query
                                                )
                                        ) {
                                            continue;
                                        }

                                        LinearLayout record =
                                                panel();

                                        TextView amount =
                                                tv(
                                                        "₹" +
                                                                money(
                                                                        x.amount
                                                                ),
                                                        20,
                                                        true
                                                );

                                        amount.setTextColor(
                                                PRIMARY
                                        );

                                        record.addView(
                                                amount
                                        );

                                        TextView route =
                                                tv(
                                                        x.fromAccount +
                                                                "  →  " +
                                                                x.toAccount,
                                                        16,
                                                        true
                                                );

                                        record.addView(
                                                route
                                        );

                                        TextView ipo =
                                                tv(
                                                        x.ipoName,
                                                        13,
                                                        false
                                                );

                                        ipo.setTextColor(
                                                MUTED
                                        );

                                        record.addView(
                                                ipo
                                        );

                                        TextView date =
                                                tv(
                                                        x.directReceived
                                                                ? "✓ Directly Received"
                                                                : "Date: " +
                                                                  x.date,
                                                        12,
                                                        true
                                                );

                                        date.setTextColor(
                                                x.directReceived
                                                        ? GREEN
                                                        : MUTED
                                        );

                                        date.setPadding(
                                                0,
                                                dp(6),
                                                0,
                                                0
                                        );

                                        record.addView(
                                                date
                                        );

                                        TextView hint =
                                                tv(
                                                        "Tap to edit or delete",
                                                        11,
                                                        false
                                                );

                                        hint.setTextColor(
                                                MUTED
                                        );

                                        hint.setPadding(
                                                0,
                                                dp(6),
                                                0,
                                                0
                                        );

                                        record.addView(
                                                hint
                                        );

                                        record.setOnClickListener(
                                                v -> editTransfer(x)
                                        );

                                        listContainer
                                                .addView(
                                                        record
                                                );
                                    }
                                };

                        addTextWatcher(
                                search,
                                refresh
                        );

                        refresh.run();
                    });
                });
    }

    // ---------------------------------------------------------
    // EDIT TRANSFER
    // ---------------------------------------------------------

    void editTransfer(
            TransferEntity old
    ) {

        base();

        head(
                "Edit Fund Transfer",
                "Update the selected transfer"
        );

        EditText amount =
                field("Amount");

        amount.setInputType(
                InputType.TYPE_CLASS_NUMBER
        );

        amount.setText(
                String.valueOf(
                        old.amount
                )
        );

        EditText from =
                field(
                        "From Account"
                );

        from.setText(
                old.fromAccount
        );

        EditText to =
                field(
                        "To Account"
                );

        to.setText(
                old.toAccount
        );

        EditText ipo =
                field(
                        "IPO Name"
                );

        ipo.setText(
                old.ipoName
        );

        EditText date =
                field(
                        "Date"
                );

        date.setText(
                old.date
        );

        prepareDateField(
                date
        );

        CheckBox direct =
                new CheckBox(this);

        direct.setText(
                "Directly received"
        );

        direct.setTextSize(
                15
        );

        direct.setTextColor(
                TEXT
        );

        direct.setChecked(
                old.directReceived
        );

        content.addView(
                direct
        );

        button(
                "SAVE CHANGES",
                v -> {

                    String amountText =
                            amount.getText()
                                    .toString()
                                    .trim();

                    if (
                            amountText.isEmpty()
                    ) {

                        toast(
                                "Amount is required."
                        );

                        return;
                    }

                    long newAmount;

                    try {

                        newAmount =
                                Long.parseLong(
                                        amountText
                                );

                        if (
                                newAmount <= 0
                        ) {
                            throw new Exception();
                        }

                    } catch (Exception e) {

                        toast(
                                "Enter a valid amount."
                        );

                        return;
                    }

                    String newFrom =
                            from.getText()
                                    .toString()
                                    .trim();

                    String newTo =
                            to.getText()
                                    .toString()
                                    .trim();

                    String newIpo =
                            ipo.getText()
                                    .toString()
                                    .trim();

                    String newDate =
                            date.getText()
                                    .toString()
                                    .trim();

                    boolean newDirect =
                            direct.isChecked();

                    if (
                            newFrom.isEmpty()
                                    ||
                                    newTo.isEmpty()
                                    ||
                                    newIpo.isEmpty()
                    ) {

                        toast(
                                "Please fill the account and IPO fields."
                        );

                        return;
                    }

                    if (
                            !newDirect
                                    &&
                                    newDate.isEmpty()
                    ) {

                        toast(
                                "Please select a date."
                        );

                        return;
                    }

                    Executors
                            .newSingleThreadExecutor()
                            .execute(() -> {

                                db.transferDao()
                                        .updateTransfer(

                                                old.amount,
                                                old.fromAccount,
                                                old.toAccount,
                                                old.ipoName,
                                                old.date,
                                                old.directReceived,

                                                newAmount,
                                                newFrom,
                                                newTo,
                                                newIpo,
                                                newDate,
                                                newDirect
                                        );

                                runOnUiThread(() -> {

                                    toast(
                                            "Transfer updated."
                                    );

                                    transfers();
                                });
                            });
                }
        );

        deleteButton(
                "DELETE TRANSFER",
                v -> confirmDeleteTransfer(old)
        );

        secondaryButton(
                "Cancel",
                v -> transfers()
        );
    }

    // ---------------------------------------------------------
    // DELETE TRANSFER
    // ---------------------------------------------------------

    void confirmDeleteTransfer(
            TransferEntity item
    ) {

        new AlertDialog.Builder(this)

                .setTitle(
                        "Delete Fund Transfer?"
                )

                .setMessage(
                        "Are you sure you want to delete this ₹" +
                                money(
                                        item.amount
                                ) +
                                " transfer?"
                )

                .setNegativeButton(
                        "Cancel",
                        null
                )

                .setPositiveButton(
                        "Delete",
                        (dialog, which) -> {

                            Executors
                                    .newSingleThreadExecutor()
                                    .execute(() -> {

                                        db.transferDao()
                                                .deleteTransfer(
                                                        item.amount,
                                                        item.fromAccount,
                                                        item.toAccount,
                                                        item.ipoName,
                                                        item.date,
                                                        item.directReceived
                                                );

                                        runOnUiThread(() -> {

                                            toast(
                                                    "Transfer deleted."
                                            );

                                            transfers();
                                        });
                                    });
                        }
                )

                .show();
    }

    // ---------------------------------------------------------
    // ADD IPO
    // ---------------------------------------------------------

    void addProfit() {

        base();

        head(
                "Add IPO Result",
                "Save a new IPO profit or loss"
        );

        EditText name =
                field(
                        "IPO Name"
                );

        EditText amount =
                field(
                        "Amount"
                );

        amount.setInputType(
                InputType.TYPE_CLASS_NUMBER
        );

        EditText bank =
                field(
                        "Bank / Account"
                );

        EditText person =
                field(
                        "Received By"
                );

        EditText date =
                field(
                        "Date"
                );

        prepareDateField(
                date
        );

        content.addView(
                label(
                        "RESULT TYPE"
                )
        );

        Spinner type =
                new Spinner(this);

        type.setAdapter(
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_dropdown_item,
                        new String[]{
                                "PROFIT",
                                "LOSS"
                        }
                )
        );

        content.addView(
                type
        );

        button(
                "SAVE RESULT",
                v -> {

                    String ipoName =
                            name.getText()
                                    .toString()
                                    .trim();

                    String amountText =
                            amount.getText()
                                    .toString()
                                    .trim();

                    if (
                            ipoName.isEmpty()
                    ) {

                        toast(
                                "Please enter IPO name."
                        );

                        return;
                    }

                    if (
                            amountText.isEmpty()
                    ) {

                        toast(
                                "Please enter amount."
                        );

                        return;
                    }

                    long value;

                    try {

                        value =
                                Long.parseLong(
                                        amountText
                                );

                        if (
                                value <= 0
                        ) {
                            throw new Exception();
                        }

                    } catch (Exception e) {

                        toast(
                                "Enter a valid amount."
                        );

                        return;
                    }

                    String selectedDate =
                            date.getText()
                                    .toString()
                                    .trim();

                    if (
                            selectedDate.isEmpty()
                    ) {

                        toast(
                                "Please select a date."
                        );

                        return;
                    }

                    int year =
                            extractYear(
                                    selectedDate
                            );

                    ProfitEntity entity =
                            new ProfitEntity(
                                    ipoName,
                                    value,
                                    type.getSelectedItem()
                                            .toString(),
                                    bank.getText()
                                            .toString()
                                            .trim(),
                                    person.getText()
                                            .toString()
                                            .trim(),
                                    selectedDate,
                                    year
                            );

                    Executors
                            .newSingleThreadExecutor()
                            .execute(() -> {

                                db.profitDao()
                                        .insert(
                                                entity
                                        );

                                runOnUiThread(() -> {

                                    toast(
                                            "IPO result saved."
                                    );

                                    profits();
                                });
                            });
                }
        );

        secondaryButton(
                "Cancel",
                v -> profits()
        );
    }

    // ---------------------------------------------------------
    // ADD TRANSFER
    // ---------------------------------------------------------

    void addTransfer() {

        base();

        head(
                "Add Fund Transfer",
                "Save a new money movement"
        );

        EditText amount =
                field(
                        "Amount"
                );

        amount.setInputType(
                InputType.TYPE_CLASS_NUMBER
        );

        EditText from =
                field(
                        "From Account"
                );

        EditText to =
                field(
                        "To Account"
                );

        EditText ipo =
                field(
                        "IPO Name"
                );

        EditText date =
                field(
                        "Date"
                );

        prepareDateField(
                date
        );

        CheckBox direct =
                new CheckBox(this);

        direct.setText(
                "Directly received"
        );

        direct.setTextSize(
                15
        );

        direct.setTextColor(
                TEXT
        );

        content.addView(
                direct
        );

        button(
                "SAVE TRANSFER",
                v -> {

                    String amountText =
                            amount.getText()
                                    .toString()
                                    .trim();

                    if (
                            amountText.isEmpty()
                    ) {

                        toast(
                                "Please enter amount."
                        );

                        return;
                    }

                    long value;

                    try {

                        value =
                                Long.parseLong(
                                        amountText
                                );

                        if (
                                value <= 0
                        ) {
                            throw new Exception();
                        }

                    } catch (Exception e) {

                        toast(
                                "Enter a valid amount."
                        );

                        return;
                    }

                    String fromText =
                            from.getText()
                                    .toString()
                                    .trim();

                    String toText =
                            to.getText()
                                    .toString()
                                    .trim();

                    String ipoText =
                            ipo.getText()
                                    .toString()
                                    .trim();

                    String selectedDate =
                            date.getText()
                                    .toString()
                                    .trim();

                    if (
                            fromText.isEmpty()
                                    ||
                                    toText.isEmpty()
                                    ||
                                    ipoText.isEmpty()
                    ) {

                        toast(
                                "Please fill the account and IPO fields."
                        );

                        return;
                    }

                    if (
                            !direct.isChecked()
                                    &&
                                    selectedDate.isEmpty()
                    ) {

                        toast(
                                "Please select a date."
                        );

                        return;
                    }

                    TransferEntity entity =
                            new TransferEntity(
                                    value,
                                    fromText,
                                    toText,
                                    ipoText,
                                    selectedDate,
                                    direct.isChecked()
                            );

                    Executors
                            .newSingleThreadExecutor()
                            .execute(() -> {

                                db.transferDao()
                                        .insert(
                                                entity
                                        );

                                runOnUiThread(() -> {

                                    toast(
                                            "Transfer saved."
                                    );

                                    transfers();
                                });
                            });
                }
        );

        secondaryButton(
                "Cancel",
                v -> transfers()
        );
    }

    // ---------------------------------------------------------
    // COMMON FIELD
    // ---------------------------------------------------------

    EditText field(
            String hint
    ) {

        EditText e =
                new EditText(this);

        e.setHint(
                hint
        );

        e.setSingleLine(
                true
        );

        e.setTextSize(
                15
        );

        e.setTextColor(
                TEXT
        );

        e.setHintTextColor(
                Color.rgb(
                        145,
                        151,
                        163
                )
        );

        e.setPadding(
                dp(14),
                0,
                dp(14),
                0
        );

        e.setBackground(
                roundedBackground(
                        CARD,
                        BORDER
                )
        );

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(
                        -1,
                        dp(52)
                );

        p.setMargins(
                0,
                dp(5),
                0,
                dp(7)
        );

        content.addView(
                e,
                p
        );

        return e;
    }

    // ---------------------------------------------------------
    // TEXT WATCHER
    // ---------------------------------------------------------

    void addTextWatcher(
            EditText editText,
            Runnable action
    ) {

        editText.addTextChangedListener(
                new TextWatcher() {

                    @Override
                    public void beforeTextChanged(
                            CharSequence s,
                            int start,
                            int count,
                            int after
                    ) {
                    }

                    @Override
                    public void onTextChanged(
                            CharSequence s,
                            int start,
                            int before,
                            int count
                    ) {

                        action.run();
                    }

                    @Override
                    public void afterTextChanged(
                            Editable s
                    ) {
                    }
                }
        );
    }

    // ---------------------------------------------------------
    // DATE FIELD
    // ---------------------------------------------------------

    void prepareDateField(
            EditText date
    ) {

        date.setFocusable(
                false
        );

        date.setClickable(
                true
        );

        date.setOnClickListener(
                v -> showDatePicker(
                        date
                )
        );
    }

    // ---------------------------------------------------------
    // DATE PICKER
    // ---------------------------------------------------------

    void showDatePicker(
            EditText target
    ) {

        Calendar calendar =
                Calendar.getInstance();

        DatePickerDialog dialog =
                new DatePickerDialog(

                        this,

                        (view, year, month, day) -> {

                            String date =
                                    String.format(
                                            Locale.getDefault(),
                                            "%02d/%02d/%04d",
                                            day,
                                            month + 1,
                                            year
                                    );

                            target.setText(
                                    date
                            );
                        },

                        calendar.get(
                                Calendar.YEAR
                        ),

                        calendar.get(
                                Calendar.MONTH
                        ),

                        calendar.get(
                                Calendar.DAY_OF_MONTH
                        )
                );

        dialog.show();
    }

    // ---------------------------------------------------------
    // YEAR
    // ---------------------------------------------------------

    int extractYear(
            String date
    ) {

        try {

            if (
                    date.length() >= 4
            ) {

                return Integer.parseInt(
                        date.substring(
                                date.length() - 4
                        )
                );
            }

        } catch (Exception ignored) {
        }

        return Calendar
                .getInstance()
                .get(
                        Calendar.YEAR
                );
    }

    void exportData() {

        Intent intent =
                new Intent(Intent.ACTION_CREATE_DOCUMENT);

        intent.addCategory(
                Intent.CATEGORY_OPENABLE
        );

        intent.setType(
                "application/json"
        );

        String fileName;

        try {

            fileName =
                    "IPO_Manager_Backup_" +
                            new SimpleDateFormat(
                                    "yyyyMMdd_HHmmss",
                                    Locale.getDefault()
                            ).format(
                                    new Date()
                            ) +
                            ".json";

        }
        catch (Exception e) {

            fileName =
                    "IPO_Manager_Backup.json";
        }

        intent.putExtra(
                Intent.EXTRA_TITLE,
                fileName
        );

        startActivityForResult(
                intent,
                REQUEST_EXPORT_DATA
        );
    }

    void performExport(Uri uri) {

        Executors
                .newSingleThreadExecutor()
                .execute(() -> {

                    try {

                        List<ProfitEntity> profits =
                                db.profitDao().getAll();

                        List<TransferEntity> transfers =
                                db.transferDao().getAll();

                        JSONObject backup =
                                new JSONObject();

                        backup.put(
                                "app",
                                "IPO Manager"
                        );

                        backup.put(
                                "backupVersion",
                                1
                        );

                        backup.put(
                                "createdAt",
                                System.currentTimeMillis()
                        );

                        // -----------------------------------------
                        // PROFITS
                        // -----------------------------------------

                        JSONArray profitArray =
                                new JSONArray();

                        for (
                                ProfitEntity p :
                                profits
                        ) {

                            JSONObject obj =
                                    new JSONObject();

                            obj.put(
                                    "ipoName",
                                    p.ipoName
                            );

                            obj.put(
                                    "amount",
                                    p.amount
                            );

                            obj.put(
                                    "type",
                                    p.type
                            );

                            obj.put(
                                    "bank",
                                    p.bank
                            );

                            obj.put(
                                    "person",
                                    p.person
                            );

                            obj.put(
                                    "date",
                                    p.date
                            );

                            obj.put(
                                    "year",
                                    p.year
                            );

                            profitArray.put(obj);
                        }

                        backup.put(
                                "profits",
                                profitArray
                        );

                        // -----------------------------------------
                        // TRANSFERS
                        // -----------------------------------------

                        JSONArray transferArray =
                                new JSONArray();

                        for (
                                TransferEntity t :
                                transfers
                        ) {

                            JSONObject obj =
                                    new JSONObject();

                            obj.put(
                                    "amount",
                                    t.amount
                            );

                            obj.put(
                                    "fromAccount",
                                    t.fromAccount
                            );

                            obj.put(
                                    "toAccount",
                                    t.toAccount
                            );

                            obj.put(
                                    "ipoName",
                                    t.ipoName
                            );

                            obj.put(
                                    "date",
                                    t.date
                            );

                            obj.put(
                                    "directReceived",
                                    t.directReceived
                            );

                            transferArray.put(obj);
                        }

                        backup.put(
                                "transfers",
                                transferArray
                        );

                        // -----------------------------------------
                        // WRITE FILE
                        // -----------------------------------------

                        try (
                                OutputStream output =
                                        getContentResolver()
                                                .openOutputStream(uri)
                        ) {

                            if (output == null) {
                                throw new Exception(
                                        "Unable to open file."
                                );
                            }

                            output.write(
                                    backup
                                            .toString(4)
                                            .getBytes(
                                                    "UTF-8"
                                            )
                            );

                            output.flush();
                        }

                        runOnUiThread(() -> {

                            Toast.makeText(
                                    this,
                                    "Data exported successfully.",
                                    Toast.LENGTH_LONG
                            ).show();

                        });

                    }
                    catch (Exception e) {

                        runOnUiThread(() -> {

                            new android.app.AlertDialog.Builder(this)
                                    .setTitle(
                                            "Export Failed"
                                    )
                                    .setMessage(
                                            e.getMessage() != null
                                                    ? e.getMessage()
                                                    : "Unable to export data."
                                    )
                                    .setPositiveButton(
                                            "OK",
                                            null
                                    )
                                    .show();

                        });
                    }
                });
    }

    void importData() {

        Intent intent =
                new Intent(Intent.ACTION_OPEN_DOCUMENT);

        intent.addCategory(
                Intent.CATEGORY_OPENABLE
        );

        intent.setType(
                "application/json"
        );

        startActivityForResult(
                intent,
                REQUEST_IMPORT_DATA
        );
    }

    void performImport(Uri uri) {

        Executors
                .newSingleThreadExecutor()
                .execute(() -> {

                    try {

                        // -----------------------------------------
                        // READ FILE
                        // -----------------------------------------

                        StringBuilder builder =
                                new StringBuilder();

                        try (
                                InputStream input =
                                        getContentResolver()
                                                .openInputStream(uri);

                                BufferedReader reader =
                                        new BufferedReader(
                                                new InputStreamReader(
                                                        input,
                                                        "UTF-8"
                                                )
                                        )
                        ) {

                            String line;

                            while (
                                    (line = reader.readLine())
                                            != null
                            ) {

                                builder.append(line);
                            }
                        }

                        JSONObject backup =
                                new JSONObject(
                                        builder.toString()
                                );

                        // -----------------------------------------
                        // VALIDATE FILE
                        // -----------------------------------------

                        if (
                                !"IPO Manager".equals(
                                        backup.optString("app")
                                )
                        ) {

                            throw new Exception(
                                    "This is not a valid IPO Manager backup file."
                            );
                        }

                        JSONArray profitArray =
                                backup.optJSONArray(
                                        "profits"
                                );

                        JSONArray transferArray =
                                backup.optJSONArray(
                                        "transfers"
                                );

                        if (
                                profitArray == null ||
                                        transferArray == null
                        ) {

                            throw new Exception(
                                    "Backup file is incomplete."
                            );
                        }

                        // -----------------------------------------
                        // EXISTING DATA
                        // -----------------------------------------

                        List<ProfitEntity> existingProfits =
                                db.profitDao().getAll();

                        List<TransferEntity> existingTransfers =
                                db.transferDao().getAll();

                        int importedProfits = 0;
                        int skippedProfits = 0;

                        int importedTransfers = 0;
                        int skippedTransfers = 0;

                        // -----------------------------------------
                        // IMPORT PROFITS
                        // -----------------------------------------

                        for (
                                int i = 0;
                                i < profitArray.length();
                                i++
                        ) {

                            JSONObject obj =
                                    profitArray.getJSONObject(i);

                            String ipoName =
                                    obj.getString(
                                            "ipoName"
                                    );

                            long amount =
                                    obj.getLong(
                                            "amount"
                                    );

                            String type =
                                    obj.getString(
                                            "type"
                                    );

                            String bank =
                                    obj.optString(
                                            "bank",
                                            ""
                                    );

                            String person =
                                    obj.optString(
                                            "person",
                                            ""
                                    );

                            String date =
                                    obj.getString(
                                            "date"
                                    );

                            int year =
                                    obj.getInt(
                                            "year"
                                    );

                            boolean alreadyExists =
                                    false;

                            for (
                                    ProfitEntity existing :
                                    existingProfits
                            ) {

                                if (
                                        existing.ipoName.equals(
                                                ipoName
                                        )
                                                &&
                                                existing.amount == amount
                                                &&
                                                existing.type.equals(
                                                        type
                                                )
                                                &&
                                                existing.bank.equals(
                                                        bank
                                                )
                                                &&
                                                existing.person.equals(
                                                        person
                                                )
                                                &&
                                                existing.date.equals(
                                                        date
                                                )
                                                &&
                                                existing.year == year
                                ) {

                                    alreadyExists = true;
                                    break;
                                }
                            }

                            if (alreadyExists) {

                                skippedProfits++;
                                continue;
                            }

                            ProfitEntity entity =
                                    new ProfitEntity(
                                            ipoName,
                                            amount,
                                            type,
                                            bank,
                                            person,
                                            date,
                                            year
                                    );

                            db.profitDao()
                                    .insert(entity);

                            existingProfits.add(entity);

                            importedProfits++;
                        }

                        // -----------------------------------------
                        // IMPORT TRANSFERS
                        // -----------------------------------------

                        for (
                                int i = 0;
                                i < transferArray.length();
                                i++
                        ) {

                            JSONObject obj =
                                    transferArray.getJSONObject(i);

                            long amount =
                                    obj.getLong(
                                            "amount"
                                    );

                            String fromAccount =
                                    obj.getString(
                                            "fromAccount"
                                    );

                            String toAccount =
                                    obj.getString(
                                            "toAccount"
                                    );

                            String ipoName =
                                    obj.getString(
                                            "ipoName"
                                    );

                            String date =
                                    obj.optString(
                                            "date",
                                            ""
                                    );

                            boolean directReceived =
                                    obj.optBoolean(
                                            "directReceived",
                                            false
                                    );

                            boolean alreadyExists =
                                    false;

                            for (
                                    TransferEntity existing :
                                    existingTransfers
                            ) {

                                if (
                                        existing.amount == amount
                                                &&
                                                existing.fromAccount.equals(
                                                        fromAccount
                                                )
                                                &&
                                                existing.toAccount.equals(
                                                        toAccount
                                                )
                                                &&
                                                existing.ipoName.equals(
                                                        ipoName
                                                )
                                                &&
                                                existing.date.equals(
                                                        date
                                                )
                                                &&
                                                existing.directReceived ==
                                                        directReceived
                                ) {

                                    alreadyExists = true;
                                    break;
                                }
                            }

                            if (alreadyExists) {

                                skippedTransfers++;
                                continue;
                            }

                            TransferEntity entity =
                                    new TransferEntity(
                                            amount,
                                            fromAccount,
                                            toAccount,
                                            ipoName,
                                            date,
                                            directReceived
                                    );

                            db.transferDao()
                                    .insert(entity);

                            existingTransfers.add(entity);

                            importedTransfers++;
                        }

                        int finalImportedProfits =
                                importedProfits;

                        int finalSkippedProfits =
                                skippedProfits;

                        int finalImportedTransfers =
                                importedTransfers;

                        int finalSkippedTransfers =
                                skippedTransfers;

                        // -----------------------------------------
                        // SHOW RESULT
                        // -----------------------------------------

                        runOnUiThread(() -> {

                            new android.app.AlertDialog.Builder(this)
                                    .setTitle(
                                            "Import Complete"
                                    )
                                    .setMessage(
                                            "IPO Profits imported: "
                                                    + finalImportedProfits
                                                    + "\n"
                                                    + "IPO Profits already present: "
                                                    + finalSkippedProfits
                                                    + "\n\n"
                                                    + "Fund Transfers imported: "
                                                    + finalImportedTransfers
                                                    + "\n"
                                                    + "Fund Transfers already present: "
                                                    + finalSkippedTransfers
                                    )
                                    .setPositiveButton(
                                            "View Data",
                                            (dialog, which) -> {

                                                home();
                                            }
                                    )
                                    .setNegativeButton(
                                            "OK",
                                            null
                                    )
                                    .show();

                        });

                    }
                    catch (Exception e) {

                        runOnUiThread(() -> {

                            new android.app.AlertDialog.Builder(this)
                                    .setTitle(
                                            "Import Failed"
                                    )
                                    .setMessage(
                                            e.getMessage() != null
                                                    ? e.getMessage()
                                                    : "Unable to import data."
                                    )
                                    .setPositiveButton(
                                            "OK",
                                            null
                                    )
                                    .show();

                        });
                    }
                });
    }


    // ---------------------------------------------------------
    // SETTINGS
    // ---------------------------------------------------------

    void settings() {

        selectedNav = 3;

        base();

        head(
                "Settings",
                "IPO Manager"
        );

        LinearLayout app =
                panel();

        TextView appName =
                tv(
                        "IPO Manager",
                        20,
                        true
                );

        app.addView(
                appName
        );

        TextView description =
                tv(
                        "Personal offline IPO management application",
                        13,
                        false
                );

        description.setTextColor(
                MUTED
        );

        app.addView(
                description
        );

        TextView version =
                tv(
                        "Version 0.5.0",
                        12,
                        true
                );

        version.setTextColor(
                PRIMARY
        );

        version.setPadding(
                0,
                dp(8),
                0,
                0
        );

        app.addView(
                version
        );

        addPanel(
                app
        );

        LinearLayout database =
                panel();

        TextView dbTitle =
                tv(
                        "LOCAL DATABASE",
                        11,
                        true
                );

        dbTitle.setTextColor(
                MUTED
        );

        database.addView(
                dbTitle
        );

        TextView dbText =
                tv(
                        "Your IPO records and fund transfers " +
                                "are stored locally on this phone " +
                                "using Room Database.\n\n" +
                                "No login, cloud account, or internet " +
                                "connection is required for the current version.",
                        14,
                        false
                );

        dbText.setTextColor(
                TEXT
        );

        dbText.setPadding(
                0,
                dp(7),
                0,
                0
        );

        database.addView(
                dbText
        );

        addPanel(
                database
        );

        button(
                "Export Data",
                v -> exportData()
        );

        button(
                "Import Data",
                v -> importData()
        );

        LinearLayout phase =
                panel();
    }
}