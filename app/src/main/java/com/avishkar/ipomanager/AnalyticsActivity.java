package com.avishkar.ipomanager;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.avishkar.ipomanager.data.AppDatabase;
import com.avishkar.ipomanager.data.ProfitEntity;
import com.avishkar.ipomanager.data.TransferEntity;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

public class AnalyticsActivity extends Activity {

    private AppDatabase db;

    private LinearLayout content;

    // ---------------------------------------------------------
    // COLORS
    // ---------------------------------------------------------

    private final int BG =
            Color.rgb(247, 248, 251);

    private final int WHITE =
            Color.WHITE;

    private final int TEXT =
            Color.rgb(30, 30, 35);

    private final int SUBTEXT =
            Color.rgb(105, 105, 110);

    private final int GREEN =
            Color.rgb(35, 150, 90);

    private final int RED =
            Color.rgb(220, 65, 65);

    private final int BLUE =
            Color.rgb(45, 100, 205);

    private final int PURPLE =
            Color.rgb(110, 70, 175);

    private final int ORANGE =
            Color.rgb(220, 125, 40);

    private final int TEAL =
            Color.rgb(30, 125, 125);

    // ---------------------------------------------------------
    // ON CREATE
    // ---------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setupSystemBars();

        db =
                AppDatabase.getInstance(this);

        buildScreen();

        loadAnalytics();
    }

    // ---------------------------------------------------------
    // SYSTEM BAR / NOTIFICATION BAR
    // ---------------------------------------------------------

    private void setupSystemBars() {

        Window window =
                getWindow();

        window.setStatusBarColor(
                BG
        );

        window.setNavigationBarColor(
                BG
        );

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            int flags =
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                flags |=
                        View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
            }

            window.getDecorView()
                    .setSystemUiVisibility(flags);
        }

        /*
         * Android 15 / Android 16 can use edge-to-edge.
         * We therefore apply the actual system-bar insets
         * directly to the root layout.
         */
    }

    // ---------------------------------------------------------
    // DP
    // ---------------------------------------------------------

    private int dp(int value) {

        return (int) (
                value *
                        getResources()
                                .getDisplayMetrics()
                                .density
                        + 0.5f
        );
    }

    // ---------------------------------------------------------
    // MONEY
    // ---------------------------------------------------------

    private String money(long amount) {

        return NumberFormat
                .getNumberInstance(
                        new Locale("en", "IN")
                )
                .format(amount);
    }

    // ---------------------------------------------------------
    // MAIN SCREEN
    // ---------------------------------------------------------

    private void buildScreen() {

        final LinearLayout root =
                new LinearLayout(this);

        root.setOrientation(
                LinearLayout.VERTICAL
        );

        root.setBackgroundColor(
                BG
        );

        /*
         * IMPORTANT:
         * The root itself receives the notification-bar
         * and navigation-bar insets.
         */
        root.setOnApplyWindowInsetsListener(
                (v, insets) -> {

                    int topInset = 0;
                    int bottomInset = 0;

                    if (Build.VERSION.SDK_INT >= 30) {

                        android.graphics.Insets systemInsets =
                                insets.getInsets(
                                        WindowInsets.Type.systemBars()
                                );

                        topInset =
                                systemInsets.top;

                        bottomInset =
                                systemInsets.bottom;

                    } else {

                        topInset =
                                insets.getSystemWindowInsetTop();

                        bottomInset =
                                insets.getSystemWindowInsetBottom();
                    }

                    /*
                     * Only apply the top inset to the header
                     * area and bottom inset to the content.
                     */
                    v.setPadding(
                            0,
                            topInset,
                            0,
                            0
                    );

                    if (content != null) {

                        content.setPadding(
                                dp(14),
                                dp(8),
                                dp(14),
                                dp(12) + bottomInset
                        );
                    }

                    return insets;
                }
        );

        // -----------------------------------------------------
        // HEADER
        // -----------------------------------------------------

        LinearLayout header =
                new LinearLayout(this);

        header.setOrientation(
                LinearLayout.HORIZONTAL
        );

        header.setGravity(
                Gravity.CENTER_VERTICAL
        );

        header.setPadding(
                dp(12),
                0,
                dp(12),
                0
        );

        // Back button

        TextView back =
                new TextView(this);

        back.setText("‹");

        back.setTextSize(
                34
        );

        back.setTextColor(
                TEXT
        );

        back.setGravity(
                Gravity.CENTER
        );

        back.setTypeface(
                Typeface.DEFAULT
        );

        LinearLayout.LayoutParams backParams =
                new LinearLayout.LayoutParams(
                        dp(38),
                        dp(50)
                );

        header.addView(
                back,
                backParams
        );

        back.setOnClickListener(
                v -> finish()
        );

        // Title

        TextView title =
                new TextView(this);

        title.setText(
                "Analytics"
        );

        title.setTextSize(
                22
        );

        title.setTextColor(
                TEXT
        );

        title.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        title.setGravity(
                Gravity.CENTER_VERTICAL
        );

        header.addView(
                title,
                new LinearLayout.LayoutParams(
                        0,
                        dp(50),
                        1
                )
        );

        root.addView(
                header,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(50)
                )
        );

        // -----------------------------------------------------
        // SCROLL AREA
        // -----------------------------------------------------

        ScrollView scroll =
                new ScrollView(this);

        scroll.setFillViewport(
                true
        );

        scroll.setClipToPadding(
                false
        );

        content =
                new LinearLayout(this);

        content.setOrientation(
                LinearLayout.VERTICAL
        );

        content.setPadding(
                dp(14),
                dp(8),
                dp(14),
                dp(12)
        );

        scroll.addView(
                content,
                new ScrollView.LayoutParams(
                        -1,
                        -2
                )
        );

        root.addView(
                scroll,
                new LinearLayout.LayoutParams(
                        -1,
                        0,
                        1
                )
        );

        setContentView(root);

        /*
         * Request the system insets after the view has
         * been attached.
         */
        root.requestApplyInsets();
    }

    // ---------------------------------------------------------
    // LOAD ANALYTICS
    // ---------------------------------------------------------

    private void loadAnalytics() {

        new Thread(() -> {

            List<ProfitEntity> profits =
                    db.profitDao().getAll();

            List<TransferEntity> transfers =
                    db.transferDao().getAll();

            long totalProfit = 0;

            long totalLoss = 0;

            // -------------------------------------------------
            // PROFIT / LOSS
            // -------------------------------------------------

            for (ProfitEntity item :
                    profits) {

                if ("LOSS".equalsIgnoreCase(
                        item.type
                )) {

                    totalLoss +=
                            item.amount;

                } else {

                    totalProfit +=
                            item.amount;
                }
            }

            long netProfit =
                    totalProfit -
                            totalLoss;

            // -------------------------------------------------
            // TRANSFERS
            // -------------------------------------------------

            long transferredAmount = 0;

            for (TransferEntity item :
                    transfers) {

                transferredAmount +=
                        item.amount;
            }

            final long finalProfit =
                    totalProfit;

            final long finalLoss =
                    totalLoss;

            final long finalNet =
                    netProfit;

            final long finalTransferred =
                    transferredAmount;

            // -------------------------------------------------
            // UI
            // -------------------------------------------------

            runOnUiThread(() -> {

                content.removeAllViews();

                // =============================================
                // OVERVIEW
                // =============================================

                sectionTitle(
                        "Overview"
                );

                // Row 1

                LinearLayout row1 =
                        overviewRow();

                statCard(
                        row1,
                        "Total Profit",
                        "₹" +
                                money(finalProfit),
                        GREEN
                );

                statCard(
                        row1,
                        "Total Loss",
                        "₹" +
                                money(finalLoss),
                        RED
                );

                content.addView(
                        row1
                );

                // Row 2

                LinearLayout row2 =
                        overviewRow();

                statCard(
                        row2,
                        "Net Profit",
                        "₹" +
                                money(finalNet),
                        BLUE
                );

                statCard(
                        row2,
                        "IPO Results",
                        String.valueOf(
                                profits.size()
                        ),
                        PURPLE
                );

                content.addView(
                        row2
                );

                // Row 3

                LinearLayout row3 =
                        overviewRow();

                statCard(
                        row3,
                        "Transfers",
                        String.valueOf(
                                transfers.size()
                        ),
                        ORANGE
                );

                statCard(
                        row3,
                        "Transferred Amount",
                        "₹" +
                                money(
                                        finalTransferred
                                ),
                        TEAL
                );

                content.addView(
                        row3
                );

                // =============================================
                // YEAR-WISE
                // =============================================

                sectionTitle(
                        "Year-wise Summary"
                );

                Map<Integer, long[]> yearly =
                        new TreeMap<>(
                                (a, b) ->
                                        Integer.compare(
                                                b,
                                                a
                                        )
                        );

                for (ProfitEntity item :
                        profits) {

                    if (!yearly.containsKey(
                            item.year
                    )) {

                        yearly.put(
                                item.year,
                                new long[]{
                                        0,
                                        0
                                }
                        );
                    }

                    if ("LOSS".equalsIgnoreCase(
                            item.type
                    )) {

                        yearly.get(
                                item.year
                        )[1] +=
                                item.amount;

                    } else {

                        yearly.get(
                                item.year
                        )[0] +=
                                item.amount;
                    }
                }

                for (
                        Map.Entry<Integer, long[]> entry :
                        yearly.entrySet()
                ) {

                    addSummaryCard(
                            String.valueOf(
                                    entry.getKey()
                            ),
                            entry.getValue()[0],
                            entry.getValue()[1]
                    );
                }

                // =============================================
                // PERSON-WISE
                // =============================================

                sectionTitle(
                        "Person-wise Summary"
                );

                Map<String, long[]> personMap =
                        new TreeMap<>(
                                String.CASE_INSENSITIVE_ORDER
                        );

                for (ProfitEntity item :
                        profits) {

                    String person;

                    if (item.person == null ||
                            item.person.trim()
                                    .isEmpty()) {

                        person =
                                "Unknown";

                    } else {

                        person =
                                item.person;
                    }

                    if (!personMap.containsKey(
                            person
                    )) {

                        personMap.put(
                                person,
                                new long[]{
                                        0,
                                        0
                                }
                        );
                    }

                    if ("LOSS".equalsIgnoreCase(
                            item.type
                    )) {

                        personMap.get(
                                person
                        )[1] +=
                                item.amount;

                    } else {

                        personMap.get(
                                person
                        )[0] +=
                                item.amount;
                    }
                }

                for (
                        Map.Entry<String, long[]> entry :
                        personMap.entrySet()
                ) {

                    addSummaryCard(
                            entry.getKey(),
                            entry.getValue()[0],
                            entry.getValue()[1]
                    );
                }

                // =============================================
                // BACK BUTTON
                // =============================================

                Button backButton =
                        new Button(this);

                backButton.setText(
                        "←  Back to Home"
                );

                backButton.setAllCaps(
                        false
                );

                backButton.setTextSize(
                        13
                );

                backButton.setTextColor(
                        Color.rgb(
                                45,
                                45,
                                45
                        )
                );

                backButton.setGravity(
                        Gravity.CENTER
                );

                backButton.setOnClickListener(
                        v -> finish()
                );

                LinearLayout.LayoutParams backParams =
                        new LinearLayout.LayoutParams(
                                -1,
                                dp(44)
                        );

                backParams.topMargin =
                        dp(6);

                content.addView(
                        backButton,
                        backParams
                );
            });

        }).start();
    }

    // ---------------------------------------------------------
    // SECTION TITLE
    // ---------------------------------------------------------

    private void sectionTitle(
            String text
    ) {

        TextView title =
                new TextView(this);

        title.setText(
                text
        );

        title.setTextSize(
                17
        );

        title.setTextColor(
                TEXT
        );

        title.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        title.setGravity(
                Gravity.START |
                        Gravity.CENTER_VERTICAL
        );

        title.setPadding(
                0,
                dp(6),
                0,
                dp(5)
        );

        content.addView(
                title,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(32)
                )
        );
    }

    // ---------------------------------------------------------
    // OVERVIEW ROW
    // ---------------------------------------------------------

    private LinearLayout overviewRow() {

        LinearLayout row =
                new LinearLayout(this);

        row.setOrientation(
                LinearLayout.HORIZONTAL
        );

        row.setGravity(
                Gravity.CENTER_VERTICAL
        );

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        -1,
                        dp(68)
                );

        params.bottomMargin =
                dp(5);

        row.setLayoutParams(
                params
        );

        return row;
    }

    // ---------------------------------------------------------
    // STAT CARD
    // ---------------------------------------------------------

    private void statCard(
            LinearLayout row,
            String title,
            String value,
            int valueColor
    ) {

        LinearLayout card =
                new LinearLayout(this);

        card.setOrientation(
                LinearLayout.VERTICAL
        );

        card.setGravity(
                Gravity.CENTER_VERTICAL
        );

        card.setPadding(
                dp(11),
                dp(5),
                dp(11),
                dp(5)
        );

        GradientDrawable background =
                new GradientDrawable();

        background.setColor(
                WHITE
        );

        background.setCornerRadius(
                dp(12)
        );

        background.setStroke(
                dp(1),
                Color.rgb(
                        232,
                        233,
                        237
                )
        );

        card.setBackground(
                background
        );

        LinearLayout.LayoutParams cardParams =
                new LinearLayout.LayoutParams(
                        0,
                        -1,
                        1
                );

        cardParams.setMargins(
                dp(2),
                dp(1),
                dp(2),
                dp(1)
        );

        row.addView(
                card,
                cardParams
        );

        TextView titleView =
                new TextView(this);

        titleView.setText(
                title
        );

        titleView.setTextSize(
                11
        );

        titleView.setTextColor(
                SUBTEXT
        );

        titleView.setGravity(
                Gravity.START
        );

        card.addView(
                titleView
        );

        TextView valueView =
                new TextView(this);

        valueView.setText(
                value
        );

        valueView.setTextSize(
                16
        );

        valueView.setTextColor(
                valueColor
        );

        valueView.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        valueView.setGravity(
                Gravity.START
        );

        LinearLayout.LayoutParams valueParams =
                new LinearLayout.LayoutParams(
                        -1,
                        -2
                );

        valueParams.topMargin =
                dp(2);

        card.addView(
                valueView,
                valueParams
        );
    }

    // ---------------------------------------------------------
    // SUMMARY CARD
    // ---------------------------------------------------------

    private void addSummaryCard(
            String title,
            long profit,
            long loss
    ) {

        long net =
                profit - loss;

        LinearLayout card =
                new LinearLayout(this);

        card.setOrientation(
                LinearLayout.VERTICAL
        );

        card.setPadding(
                dp(12),
                dp(7),
                dp(12),
                dp(7)
        );

        GradientDrawable background =
                new GradientDrawable();

        background.setColor(
                WHITE
        );

        background.setCornerRadius(
                dp(12)
        );

        background.setStroke(
                dp(1),
                Color.rgb(
                        232,
                        233,
                        237
                )
        );

        card.setBackground(
                background
        );

        LinearLayout.LayoutParams cardParams =
                new LinearLayout.LayoutParams(
                        -1,
                        -2
                );

        cardParams.setMargins(
                0,
                dp(1),
                0,
                dp(5)
        );

        content.addView(
                card,
                cardParams
        );

        // TITLE

        TextView titleView =
                new TextView(this);

        titleView.setText(
                title
        );

        titleView.setTextSize(
                15
        );

        titleView.setTextColor(
                TEXT
        );

        titleView.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        card.addView(
                titleView
        );

        // PROFIT

        TextView profitView =
                new TextView(this);

        profitView.setText(
                "Profit: ₹" +
                        money(profit)
        );

        profitView.setTextSize(
                12
        );

        profitView.setTextColor(
                GREEN
        );

        LinearLayout.LayoutParams profitParams =
                new LinearLayout.LayoutParams(
                        -1,
                        -2
                );

        profitParams.topMargin =
                dp(2);

        card.addView(
                profitView,
                profitParams
        );

        // LOSS

        TextView lossView =
                new TextView(this);

        lossView.setText(
                "Loss: ₹" +
                        money(loss)
        );

        lossView.setTextSize(
                12
        );

        lossView.setTextColor(
                RED
        );

        card.addView(
                lossView
        );

        // NET

        TextView netView =
                new TextView(this);

        netView.setText(
                "Net: ₹" +
                        money(net)
        );

        netView.setTextSize(
                12
        );

        netView.setTextColor(
                BLUE
        );

        netView.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        card.addView(
                netView
        );
    }
}