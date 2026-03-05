# Postman + Newman Usage

## Files
- `FarmcoConnect_Practical_Exam.postman_collection.json`
- `FarmcoConnect_Local.postman_environment.json`

## Import in Postman
1. Open Postman.
2. Import both JSON files from this folder.
3. Select environment `Farmco Connect Local`.
4. Run the collection in order from request `01` to `21`.

The collection scripts automatically store ids in environment variables:
- `provinceId`, `districtId`, `sectorId`, `cellId`, `villageId`
- `userId`, `farmerId`, `harvestId`

## Generate one HTML evidence report (no screenshots)

From `backend` folder, run:

```powershell
npm install -g newman newman-reporter-htmlextra
newman run .\postman\FarmcoConnect_Practical_Exam.postman_collection.json `
  -e .\postman\FarmcoConnect_Local.postman_environment.json `
  -r cli,htmlextra,json `
  --reporter-htmlextra-export .\postman\Farmco_Test_Report.html `
  --reporter-json-export .\postman\Farmco_Test_Report.json
```

Output:
- `postman/Farmco_Test_Report.html` (submit this as marking evidence)
- `postman/Farmco_Test_Report.json`
