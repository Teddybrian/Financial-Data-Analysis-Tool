
package finance.data.analysis.project;



import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FinanceDataAnalysisProject extends JFrame {
    private List<Double> data;

    public FinanceDataAnalysisProject() {
        setTitle("Financial Data Analysis Tool");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        data = new ArrayList<>();

        JPanel contentPane = new JPanel(new BorderLayout());
        setContentPane(contentPane);

        JTextArea outputTextArea = new JTextArea();
        outputTextArea.setEditable(false);
        contentPane.add(new JScrollPane(outputTextArea), BorderLayout.CENTER);

        JButton fetchDataButton = new JButton("Fetch Data");
        fetchDataButton.addActionListener(e -> {
            fetchData();
            outputTextArea.setText("Data fetched successfully.");
        });
        contentPane.add(fetchDataButton, BorderLayout.NORTH);

        JButton calculateReturnsButton = new JButton("Calculate Returns");
        calculateReturnsButton.addActionListener(e -> {
            if (data.size() >= 2) {
                List<Double> returns = calculateReturns();
                outputTextArea.setText("Returns:\n" + returns);
            } else {
                outputTextArea.setText("Insufficient data to calculate returns.");
            }
        });
        contentPane.add(calculateReturnsButton, BorderLayout.WEST);

        JButton calculateVolatilityButton = new JButton("Calculate Volatility");
        calculateVolatilityButton.addActionListener(e -> {
            if (data.size() >= 2) {
                double volatility = calculateVolatility();
                outputTextArea.setText("Volatility: " + volatility);
            } else {
                outputTextArea.setText("Insufficient data to calculate volatility.");
            }
        });
        contentPane.add(calculateVolatilityButton, BorderLayout.CENTER);

        JButton calculateCorrelationButton = new JButton("Calculate Correlation");
        calculateCorrelationButton.addActionListener(e -> {
            if (data.size() >= 2) {
                double correlation = calculateCorrelation();
                outputTextArea.setText("Correlation: " + correlation);
            } else {
                outputTextArea.setText("Insufficient data to calculate correlation.");
            }
        });
        contentPane.add(calculateCorrelationButton, BorderLayout.EAST);
    }

    private void fetchData() {
        try {
            URL url = new URL("https://shorturl.at/itz57");
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                double value = Double.parseDouble(line);
                data.add(value);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

    private List<Double> calculateReturns() {
        List<Double> returns = new ArrayList<>();
        for (int i = 1; i < data.size(); i++) {
            double previousValue = data.get(i - 1);
            double currentValue = data.get(i);
            double returnPercentage = (currentValue - previousValue) / previousValue * 100.0;
            returns.add(returnPercentage);
        }
        return returns;
    }

    private double calculateVolatility() {
        double sum = 0.0;
        for (double value : data) {
            sum += value;
        }
        double average = sum / data.size();

        double squaredSum = 0.0;
        for (double value : data) {
            squaredSum += Math.pow(value - average, 2);
        }
        double variance = squaredSum / data.size();

        return Math.sqrt(variance);
    }

    private double calculateCorrelation() {
        double sumXY = 0.0;
        double sumX = 0.0;
        double sumY = 0.0;
        double sumXSquare = 0.0;
        double sumYSquare = 0.0;

        for (int i = 0; i < data.size() - 1; i++) {
            double x = data.get(i);
            double y = data.get(i + 1);
            sumXY += x * y;
            sumX += x;
            sumY += y;
            sumXSquare += x * x;
            sumYSquare += y * y;
        }

        int n = data.size() - 1;
        double numerator = n * sumXY - sumX * sumY;
        double denominator = Math.sqrt((n * sumXSquare - sumX * sumX) * (n * sumYSquare - sumY * sumY));

        return numerator / denominator;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FinanceDataAnalysisProject tool = new FinanceDataAnalysisProject();
            tool.setVisible(true);
        });
    }
}
