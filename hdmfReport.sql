SELECT e.hdmfNo AS hdmfNo, 
CONCAT_WS(', ', setNameToUpper(e.lastname), 
CONCAT_WS(' ', setNameToUpper(e.firstname), setNameToUpper(e.middlename))) AS name, 
s.hdmf AS hdmf, 
CASE WHEN (SELECT employmentWageStatus FROM employee WHERE employeeId = e.employeeId) = 
'minimum' THEN (if(s.hdmf = 0, 0, 100)) ELSE IF(s.hdmf = 100, 100, if(s.hdmf = 0, 0, ROUND((getBasicSalary(e.employmentWageEntry, e.employmentWage)*.02), 0))) END AS erHdmf
, b.name AS branch, 
tn.name AS tradeName, 
cn.name AS corporateName, 
tn.hdmfNo AS erHdmfNo, 
b.address AS address, 
s.payrollDate AS payrollDate 
FROM employee e INNER JOIN payroll_table s ON e.employeeId = s.employeeId 
INNER JOIN branch_table b ON e.branchId = b.id 
INNER JOIN trade_table tn ON b.tradeId = tn.id 
INNER JOIN corporate_table cn ON tn.corporateId = cn.id 
WHERE (SELECT branchId FROM employee_contribution_main 
WHERE employeeId = e.employeeId ORDER BY id DESC LIMIT 1) = $P{BRANCH_ID} AND s.payrollDate = $P{PAYROLL_DATE} AND 
(s.actionTaken = 'adjusted' OR s.actionTaken IS NULL)