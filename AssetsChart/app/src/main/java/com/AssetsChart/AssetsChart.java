package com.AssetsChart;

import android.app.Activity;
import android.widget.TextView;
import android.os.Bundle;
import android.os.Environment;
import android.widget.LinearLayout;
import android.view.Gravity;
import android.graphics.Color;
import java.util.List;
import java.util.ArrayList;
import android.graphics.DashPathEffect;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.formatter.*;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Button;
import android.view.View;


public class AssetsChart extends Activity 
{
    /** アクティビティが最初に作成されるときに呼び出されます。 */
    DateManager dateManager = new DateManager();

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assets_chart);

        dateManager.getNowYearMonth();
        dateManager.setYearMonth(dateManager.yyyy, dateManager.mm);
        ChartDisp(getApplicationContext());

        findViewById(R.id.previous_month).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateManager.setPrevMonth();
                ChartDisp(getApplicationContext());
            }
        });

        findViewById(R.id.next_month).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateManager.setNextMonth();
                ChartDisp(getApplicationContext());
            }
        });


    }

    public void ChartDisp(Context context)
    {
        LineChart Chart = (LineChart) findViewById(R.id.lineChart);

        Chart.getAxisRight().setEnabled(false);
        Chart.getAxisLeft().setEnabled(true);
        Chart.setDrawGridBackground(false);
        Chart.setEnabled(true);
        Chart.setTouchEnabled(true);
        Chart.setPinchZoom(true);
        Chart.setDoubleTapToZoomEnabled(true);
        Chart.setBackgroundColor(Color.TRANSPARENT);
        Chart.setScaleEnabled(true);
        Chart.getLegend().setEnabled(true);

        //X軸周り
        XAxis xAxis = Chart.getXAxis();
        xAxis.setDrawLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(true);

        ArrayList<Entry> values_total = new ArrayList<Entry>();
        ArrayList<Entry> values_cash = new ArrayList<Entry>();
        ArrayList<Entry> values_stocks = new ArrayList<Entry>();
        ArrayList<Entry> values_invest = new ArrayList<Entry>();
        ArrayList<Entry> values_points = new ArrayList<Entry>();

        AssetsChart.CsvRead readData = new AssetsChart.CsvRead();
        readData.reader(context);

        if(readData.readOk == true)
        {
            for(int i=0;i<readData.assetList.size();i++)
            {
               values_total.add(new Entry(i,readData.assetList.get(i).total));
               values_cash.add(new Entry(i,readData.assetList.get(i).cash));
               values_stocks.add(new Entry(i,readData.assetList.get(i).stocks));
               values_invest.add(new Entry(i,readData.assetList.get(i).invest));
               values_points.add(new Entry(i,readData.assetList.get(i).points));
            }
            IndexAxisValueFormatter indexFormatter = new IndexAxisValueFormatter(readData.getDateList());
            xAxis.setValueFormatter(indexFormatter);

            ListTitleData legend = new ListTitleData();

            LineDataSet values_totalDataSet = new LineDataSet(values_total, legend.legend_total);
            values_totalDataSet.setColor(ColorTemplate.COLORFUL_COLORS[0]);
            values_totalDataSet.setLineWidth(2);
            values_totalDataSet.setDrawCircles(false);
 
            LineDataSet values_cashDataSet = new LineDataSet(values_cash, legend.legend_cash);
            values_cashDataSet.setColor(ColorTemplate.COLORFUL_COLORS[1]);
            values_cashDataSet.setLineWidth(2);
            values_cashDataSet.setDrawCircles(false);
 
            LineDataSet values_stocksDataSet = new LineDataSet(values_stocks, legend.legend_stocks);
            values_stocksDataSet.setColor(ColorTemplate.COLORFUL_COLORS[2]);
            values_stocksDataSet.setLineWidth(2);
            values_stocksDataSet.setDrawCircles(false);
 
            LineDataSet values_investDataSet = new LineDataSet(values_invest, legend.legend_invest);
            values_investDataSet.setColor(ColorTemplate.COLORFUL_COLORS[3]);
            values_investDataSet.setLineWidth(2);
            values_investDataSet.setDrawCircles(false);
 
            LineDataSet values_pointsDataSet = new LineDataSet(values_points, legend.legend_points);
            values_pointsDataSet.setColor(ColorTemplate.COLORFUL_COLORS[4]);
            values_pointsDataSet.setLineWidth(2);
            values_pointsDataSet.setDrawCircles(false);

            LineData lineData = new LineData();
            lineData.addDataSet(values_totalDataSet);
            lineData.addDataSet(values_cashDataSet);
            lineData.addDataSet(values_stocksDataSet);
            lineData.addDataSet(values_investDataSet);
            lineData.addDataSet(values_pointsDataSet);
            Chart.setData(lineData);

        }
        else
        {
            Chart.setData(null);
        }

        Chart.invalidate();
    }

    public class DateManager
    {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        int yyyy;
        int mm;

        public void getNowYearMonth() {
            yyyy = calendar.get(java.util.Calendar.YEAR);
            mm = calendar.get(java.util.Calendar.MONTH) + 1;
        }


        public void setYearMonth(int in_yyyy, int in_mm) {
            yyyy = in_yyyy;
            mm = in_mm;
            ((TextView) findViewById(R.id.header_month_text)).setText(Integer.toString(yyyy)+"年"+Integer.toString(mm)+"月");
        }

        public void setPrevMonth() {
            mm--;
            if(mm < 1)
            {
                mm = 12;
                yyyy--;
            }
            ((TextView) findViewById(R.id.header_month_text)).setText(Integer.toString(yyyy)+"年"+Integer.toString(mm)+"月");
        }

        public void setNextMonth() {
            mm++;
            if(12 < mm)
            {
                mm = 1;
                yyyy++;
            }
            ((TextView) findViewById(R.id.header_month_text)).setText(Integer.toString(yyyy)+"年"+Integer.toString(mm)+"月");
        }
    }

    public class AssetListData
    {
        String date; 
        int total; 
        int cash; 
        int stocks; 
        int invest; 
        int points; 

        public void setDate(String date) { this.date = date; }
        public String getDate() { return this.date; }

        public void setTotal(int total) { this.total = total; }
        public int getTotal() { return this.total; }

        public void setCash(int cash) { this.cash = cash; }
        public int getCach() { return this.cash; }

        public void setStocks(int stocks) { this.stocks = stocks; }
        public int getStocks() { return this.stocks; }

        public void setInvest(int invest) { this.invest = invest; }
        public int getInvest() { return this.invest; }

        public void setPoints(int points) { this.points = points; }
        public int getPoints() { return this.points; }

    }

    public class ListTitleData
    {
        String legend_date = "日付";
        String legend_total = "合計";
        String legend_cash = "預金・現金";
        String legend_stocks = "株式";
        String legend_invest = "投資信託";
        String legend_points = "ポイント";
    }

    public class CsvRead
    {
        Boolean readOk = false;

        List<AssetListData> assetList = new ArrayList<AssetListData>();

        public ArrayList<String> getDateList() {
                ArrayList<String> label = new ArrayList<String>();
                for (int i = 0; i < assetList.size(); i++)
                    label.add(assetList.get(i).date);
                return label;
            }


         public void reader(Context context) {
            String month0 = "";
            if(1 <= dateManager.mm && dateManager.mm <= 9 ){ 
                month0 = "0";
            }
            String fileName = getExternalFilesDir(null) + "/資産推移_" + Integer.toString(dateManager.yyyy) + "-" + month0 + Integer.toString(dateManager.mm) + ".csv";
            try
            {
                {
                    String debugtxtpath = getExternalFilesDir(null) + "/OpenLastFileName.txt";
                    java.io.File debugtxt = new java.io.File(debugtxtpath);
                    java.io.FileWriter fileWriter = new java.io.FileWriter(debugtxt, false);
                    java.io.BufferedWriter bufferedWriter  = new java.io.BufferedWriter(fileWriter);
                    bufferedWriter.append("open fileName:"+fileName);
                    bufferedWriter.newLine();
                    bufferedWriter.close();
                }

                java.io.FileInputStream inputStream = new java.io.FileInputStream(new java.io.File(fileName));
                java.io.InputStreamReader inputStreamReader = new java.io.InputStreamReader(inputStream);
                java.io.BufferedReader bufferReader = new java.io.BufferedReader(inputStreamReader);

                int i = 0;
                String line_org;
                while ((line_org = bufferReader.readLine()) != null) {
                	if( i != 0 )
                	{
                        AssetListData data = new AssetListData();
                        String line = line_org.replace("\"","");
                        String[] RowData = line.split(",");
                        String[] yyyymmdd = RowData[0].split("/");
                        data.setDate(yyyymmdd[2]);
                        data.setTotal(Integer.valueOf(RowData[1]));
                        data.setCash(Integer.valueOf(RowData[2]));
                        data.setStocks(Integer.valueOf(RowData[3]));
                        data.setInvest(Integer.valueOf(RowData[4]));
                        data.setPoints(Integer.valueOf(RowData[5]));
                    
                        assetList.add(data);
                	}
                    i++;
                }
                readOk = true;
                bufferReader.close();
            } catch (java.io.FileNotFoundException e) {
            	e.printStackTrace();
                readOk = false;
            } catch (java.io.IOException e) {
                readOk = false;
                e.printStackTrace();
            } finally {
                if(readOk == false)
                    return;
            }
         }
    }
}
