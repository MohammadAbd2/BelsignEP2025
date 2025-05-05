package DAL;

import BE.QCReport;
import java.util.List;

public interface IQCReportDB extends IDBConnector {
    QCReport createQCReport(QCReport report);
    QCReport getQCReportById(int id);
    List<QCReport> getAllQCReports();
    void updateQCReport(QCReport report);
    void deleteQCReport(int id);
}
