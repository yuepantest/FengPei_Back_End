package com.fengpei.web.tool;

import com.fengpei.web.entiry.CalculateData;
import com.fengpei.web.entiry.Client;

import java.sql.ResultSet;
import java.util.Objects;


public class BusinessTool {

    public FormalTool formalTool = new FormalTool();

    public CalculateData setSelectDataContent(Integer sum, Integer type, String bankId, String identityCard, long clientId,String applicationNumber) {
        CalculateData calculateData = new CalculateData();
        calculateData.type = type;
        calculateData.bankIdContent = getBankContent(bankId);
        calculateData.identityCardContent = getIdentityContent(identityCard);
        calculateData.num = sum;
        calculateData.id = clientId;
        calculateData.applicationNumber = applicationNumber;
        double number = (double) sum;
        double rate;
        if (type == 1) {
            rate = 0.008;
            calculateData.title_one = "12期";
            calculateData.content_one = "月供：" + formalTool.setStringDoubledEnd2(number / 12 + number * rate);
            calculateData.title_two = "24期";
            calculateData.content_two = "月供：" + formalTool.setStringDoubledEnd2(number / 24 + number * rate);
            calculateData.title_three = "36期";
            calculateData.content_tree = "月供：" + formalTool.setStringDoubledEnd2(number / 36 + number * rate);
            calculateData.title_four = "48期";
            calculateData.content_four = "月供：" + formalTool.setStringDoubledEnd2(number / 48 + number * rate);
            calculateData.title_five = "60期";
            calculateData.content_five = "月供：" + formalTool.setStringDoubledEnd2(number / 60 + number * rate);
            calculateData.rate = "0.8%";
        } else if (type == 2) {
            rate = 0.006;
            calculateData.title_one = "12期";
            calculateData.content_one = "月供：" + formalTool.setStringDoubledEnd2(number * rate);
            calculateData.title_two = "24期";
            calculateData.content_two = "月供：" + formalTool.setStringDoubledEnd2(number * rate);
            calculateData.title_three = "36期";
            calculateData.content_tree = "月供：" + formalTool.setStringDoubledEnd2(number * rate);
            calculateData.title_four = "60期";
            calculateData.content_four = "月供：" + formalTool.setStringDoubledEnd2(number * rate);
            calculateData.rate = "0.6%";
        } else {
            rate = 0.012;
            calculateData.title_one = "3期";
            calculateData.content_one = "月供：" + formalTool.setStringDoubledEnd2(number / 3 + number * rate);
            calculateData.title_two = "6期";
            calculateData.content_two = "月供：" + formalTool.setStringDoubledEnd2(number / 6 + number * rate);
            calculateData.title_three = "12期";
            calculateData.content_tree = "月供：" + formalTool.setStringDoubledEnd2(number / 12 + number * rate);
            calculateData.title_four = "18期";
            calculateData.content_four = "月供：" + formalTool.setStringDoubledEnd2(number / 18 + number * rate);
            calculateData.title_five = "24期";
            calculateData.content_five = "月供：" + formalTool.setStringDoubledEnd2(number / 24 + number * rate);
            calculateData.title_six = "36期";
            calculateData.content_six = "月供：" + formalTool.setStringDoubledEnd2(number / 36 + number * rate);
            calculateData.rate = "1.2%";
        }
        return calculateData;
    }

    public double countingOneStep(double annualIncome, double quantityNumber, Integer loanAmount, double estate) {
        double quantity = annualIncome / 10000 * loanAmount + quantityNumber / 6 * loanAmount + estate * 10000 / 10;
        quantity = quantity > loanAmount ? loanAmount : quantity;
        return quantity;
    }

    public int countingTwoStep(Integer antPoints, Integer creditQuery, double quantity) {
        double finalSum;
        //芝麻分计算
        if (antPoints > 699) {
            finalSum = quantity;
        } else if (antPoints > 649) {
            finalSum = quantity * 0.85;
        } else if (antPoints > 549) {
            finalSum = quantity * 0.60;
        } else {
            finalSum = 0.00;
        }
        //征信计算
        if (creditQuery == 2) {
            finalSum = finalSum * 0.8;
        } else if (creditQuery == 3) {
            finalSum = 0;
        }
        int result = (int) finalSum;
        result = result < 5000 ? 0 : result;
        return result;
    }

    public String getString(Client client, String TABLE_NAME) {
        if(Objects.equals(client.bankId, "")){
            client.bankId= "''" ;
        }
        String str = "(clientName,city,phone,identityCard,monthIncome,socialSecurity,accumulationFund,estateValue,loanAmount,antPoints,creditStatue,bankId,assessMoney,status,type,applyTime,applicationNumber) VALUES " + "(" + "'" + client.clientName + "'" + "," + "'" + client.city + "'" + "," + "'" + client.phone + "'" + "," + "'" + client.identityCard + "'" + "," + client.monthIncome + "," + client.socialSecurity + "," + client.accumulationFund + "," + client.estateValue + "," + client.loanAmount + "," + client.antPoints + "," + client.creditStatue + "," + client.bankId + "," + client.assessMoney + "," + client.status + "," + client.type + "," + "'" + client.applyTime + "'"+ "," + "'" + client.applicationNumber + "'" + ")";
        return "INSERT INTO " + TABLE_NAME + str;
    }

    public Client setClientData(ResultSet resultSet) throws Exception {
        Client client = new Client();
        client.id = resultSet.getInt("id");
        client.clientName = resultSet.getString("clientName");
        client.city = resultSet.getString("city");
        client.phone = resultSet.getString("phone");
        client.identityCard = resultSet.getString("identityCard");
        client.monthIncome = resultSet.getInt("monthIncome");
        client.socialSecurity = resultSet.getInt("socialSecurity");
        client.accumulationFund = resultSet.getInt("accumulationFund");
        client.estateValue = resultSet.getInt("estateValue");
        client.loanAmount = resultSet.getInt("loanAmount");
        client.antPoints = resultSet.getInt("antPoints");
        client.creditStatue = resultSet.getInt("creditStatue");
        client.bankId = resultSet.getString("bankId");
        client.type = resultSet.getInt("type");
        client.assessMoney = resultSet.getInt("assessMoney");
        client.status = resultSet.getInt("status");
        client.applyTime = resultSet.getString("applyTime");
        client.educationBackground = resultSet.getString("educationBackground");
        client.maritalStatus = resultSet.getString("maritalStatus");
        client.debt = resultSet.getString("debt");
        client.presentAddress = resultSet.getString("presentAddress");
        client.detailAddress = resultSet.getString("detailAddress");
        client.livingModel = resultSet.getString("livingModel");
        client.livingSpend = resultSet.getInt("livingSpend");
        client.childrenNumber = resultSet.getString("childrenNumber");
        client.relativeOneName = resultSet.getString("relativeOneName");
        client.relativeOneBetween = resultSet.getString("relativeOneBetween");
        client.relativeOnePhone = resultSet.getString("relativeOnePhone");
        client.relativeTwoName = resultSet.getString("relativeTwoName");
        client.relativeTwoBetween = resultSet.getString("relativeTwoBetween");
        client.relativeTwoPhone = resultSet.getString("relativeTwoPhone");
        client.colleagueOneName = resultSet.getString("colleagueOneName");
        client.colleagueOnePhone = resultSet.getString("colleagueOnePhone");
        client.colleagueTwoName = resultSet.getString("colleagueTwoName");
        client.colleagueTwoPhone = resultSet.getString("colleagueTwoPhone");
        client.companyname = resultSet.getString("companyname");
        client.companytype = resultSet.getString("companytype");
        client.companysector = resultSet.getString("companysector");
        client.companyposition = resultSet.getString("companyposition");
        client.companytime = resultSet.getString("companytime");
        client.leaderName = resultSet.getString("leaderName");
        client.companyScale = resultSet.getString("companyScale");
        client.monthSalary = resultSet.getString("monthSalary");
        client.acquairSalaryType = resultSet.getString("acquairSalaryType");
        client.acquairSalaryDate = resultSet.getString("acquairSalaryDate");
        client.companyAdress = resultSet.getString("companyAdress");
        client.companyPhoneNumber = resultSet.getString("companyPhoneNumber");
        client.commuteTime = resultSet.getString("commuteTime");
        client.remark = resultSet.getString("remark");
        client.submitTime = resultSet.getString("submitTime");
        client.refuseReasonTwo = resultSet.getString("refuseReasonTwo");
        client.refuseReasonOne = resultSet.getString("refuseReasonOne");
        client.applicationNumber = resultSet.getString("applicationNumber");
        return client;
    }

    public String getBankContent(String bankId) {
        if(bankId==null||bankId.isEmpty()){
            return "待补充";
        }
        String bankIdStart = formalTool.subString(bankId, 0, 4);
        String bankIdEnd = formalTool.subString(bankId, bankId.length() - 4, bankId.length());
        return bankId.length() > 8 ? bankIdStart + "  ****  " + bankIdEnd : bankId;
    }

    public String getIdentityContent(String bankId) {
        String bankIdStart = formalTool.subString(bankId, 0, 2);
        String bankIdEnd = formalTool.subString(bankId, bankId.length() - 4, bankId.length());
        return bankId.length() > 8 ? bankIdStart + "  ****  " + bankIdEnd : bankId;
    }
}
