-- V6__relational_refactoring.sql

-- 1. Insert standard departments if they don't exist
INSERT INTO department (label)
SELECT d FROM (VALUES ('INFO'), ('GEA'), ('TC'), ('MMI'), ('GMP'), ('MP')) v(d)
WHERE NOT EXISTS (SELECT 1 FROM department WHERE label = v.d);

-- 2. Add Foreign Key columns
ALTER TABLE app_user ADD COLUMN department_id UUID REFERENCES department(id);
ALTER TABLE resource_sheet ADD COLUMN department_id UUID REFERENCES department(id);
ALTER TABLE resource_sheet ADD COLUMN ue_id UUID REFERENCES ue(id);
ALTER TABLE mccc ADD COLUMN department_id UUID REFERENCES department(id);
ALTER TABLE mccc ADD COLUMN ue_id UUID REFERENCES ue(id);

-- 3. Migrate existing AppUser data
UPDATE app_user a
SET department_id = (SELECT id FROM department d WHERE d.label = a.departement LIMIT 1)
WHERE a.departement IS NOT NULL;

-- 4. Migrate existing ResourceSheet data
-- Department
UPDATE resource_sheet r
SET department_id = (SELECT id FROM department d WHERE d.label = r.departement LIMIT 1)
WHERE r.departement IS NOT NULL;

-- Create missing UEs found in resource sheets
INSERT INTO ue (title, semester, department_id)
SELECT DISTINCT ue, 1, (SELECT id FROM department LIMIT 1)
FROM resource_sheet 
WHERE ue IS NOT NULL AND ue != ''
AND NOT EXISTS (SELECT 1 FROM ue u WHERE u.title = resource_sheet.ue);

-- UE
UPDATE resource_sheet r
SET ue_id = (SELECT id FROM ue u WHERE u.title = r.ue LIMIT 1)
WHERE r.ue IS NOT NULL;

-- 5. Migrate existing MCCC data
-- Department
UPDATE mccc m
SET department_id = (SELECT id FROM department d WHERE d.label = m.department LIMIT 1)
WHERE m.department IS NOT NULL;

-- Create missing UEs found in MCCCs
INSERT INTO ue (title, semester, department_id)
SELECT DISTINCT ue, 1, (SELECT id FROM department LIMIT 1)
FROM mccc 
WHERE ue IS NOT NULL AND ue != ''
AND NOT EXISTS (SELECT 1 FROM ue u WHERE u.title = mccc.ue);

-- UE
UPDATE mccc m
SET ue_id = (SELECT id FROM ue u WHERE u.title = m.ue LIMIT 1)
WHERE m.ue IS NOT NULL;

-- 6. Drop old textual columns
ALTER TABLE app_user DROP COLUMN departement;
ALTER TABLE resource_sheet DROP COLUMN departement;
ALTER TABLE resource_sheet DROP COLUMN ue;
ALTER TABLE mccc DROP COLUMN department;
ALTER TABLE mccc DROP COLUMN ue;
