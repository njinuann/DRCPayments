/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RubyPayments;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 *
 * @author NJINU
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class TaxResponse extends BaseResponse{
     
    public  String articleGroup;
    public  String BudgetNaming;
    public  String PmtPeriod;
    public  String issueDate;
    public  String deadline;
    private String NotePerception;

    public String getArticleGroup() {
        return articleGroup;
    }

    public void setArticleGroup(String articleGroup) {
        this.articleGroup = articleGroup;
    }

    public String getBudgetNaming() {
        return BudgetNaming;
    }

    public void setBudgetNaming(String BudgetNaming) {
        this.BudgetNaming = BudgetNaming;
    }

    public String getPmtPeriod() {
        return PmtPeriod;
    }

    public void setPmtPeriod(String PmtPeriod) {
        this.PmtPeriod = PmtPeriod;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    /**
     * @return the NotePerception
     */
    public String getNotePerception() {
        return NotePerception;
    }

    /**
     * @param NotePerception the NotePerception to set
     */
    public void setNotePerception(String NotePerception) {
        this.NotePerception = NotePerception;
    }

   
    
}
