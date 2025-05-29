package DAL;

import BE.QCReport;
import DAL.Interfaces.IQCReportDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QCReportDB implements IQCReportDB {

    private final DBConnector dbConnector;

    public QCReportDB() {
        this.dbConnector = new DBConnector();
    }

    @Override
    public QCReport createQCReport(QCReport report) {
        String sql = "INSERT INTO QC_Belsign_schema.qc_report (orderNumber, qaName, status, email, frontImage, backImage, leftImage, rightImage, topImage, notes) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, report.getOrderNumber());
            stmt.setString(2, report.getQaName());
            stmt.setString(3, report.getStatus());
            stmt.setString(4, report.getEmail());
            stmt.setString(5, report.getFrontImage());
            stmt.setString(6, report.getBackImage());
            stmt.setString(7, report.getLeftImage());
            stmt.setString(8, report.getRightImage());
            stmt.setString(9, report.getTopImage());
            stmt.setString(10, report.getNotes());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                report.setId(rs.getInt(1));
                return report;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public QCReport getQCReportById(int id) {
        String sql = "SELECT * FROM QC_Belsign_schema.qc_report WHERE id = ?";
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new QCReport(
                        rs.getInt("id"),
                        rs.getString("orderNumber"),
                        rs.getString("qaName"),
                        rs.getString("status"),
                        rs.getString("email"),
                        rs.getString("frontImage"),
                        rs.getString("backImage"),
                        rs.getString("leftImage"),
                        rs.getString("rightImage"),
                        rs.getString("topImage"),
                        rs.getString("notes")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<QCReport> getAllQCReports() {
        List<QCReport> reports = new ArrayList<>();
        String sql = "SELECT * FROM QC_Belsign_schema.qc_report";
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                reports.add(new QCReport(
                        rs.getInt("id"),
                        rs.getString("orderNumber"),
                        rs.getString("qaName"),
                        rs.getString("status"),
                        rs.getString("email"),
                        rs.getString("frontImage"),
                        rs.getString("backImage"),
                        rs.getString("leftImage"),
                        rs.getString("rightImage"),
                        rs.getString("topImage"),
                        rs.getString("notes")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reports;
    }

    @Override
    public void updateQCReport(QCReport report) {
        String sql = "UPDATE QC_Belsign_schema.qc_report SET orderNumber = ?, qaName = ?, status = ?, email = ?, frontImage = ?, backImage = ?, leftImage = ?, rightImage = ?, topImage = ?, notes = ? WHERE id = ?";
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, report.getOrderNumber());
            stmt.setString(2, report.getQaName());
            stmt.setString(3, report.getStatus());
            stmt.setString(4, report.getEmail());
            stmt.setString(5, report.getFrontImage());
            stmt.setString(6, report.getBackImage());
            stmt.setString(7, report.getLeftImage());
            stmt.setString(8, report.getRightImage());
            stmt.setString(9, report.getTopImage());
            stmt.setString(10, report.getNotes());
            stmt.setInt(11, report.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void deleteQCReport(int id) {
        String sql = "DELETE FROM QC_Belsign_schema.qc_report WHERE id = ?";
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
