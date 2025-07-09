-- Cập nhật schema cho hệ thống phân quyền

-- Thêm cột password và is_department_manager vào bảng users
ALTER TABLE `users` 
ADD COLUMN `password` VARCHAR(255) DEFAULT NULL,
ADD COLUMN `is_department_manager` BOOLEAN DEFAULT FALSE;

-- Cập nhật enum role để bao gồm các quyền mới
ALTER TABLE `users` 
MODIFY COLUMN `role` ENUM('EMPL','MANA','SUP') DEFAULT 'EMPL';

INSERT INTO employee_assessment.departments (department_id,department_name,description)
VALUES (3,'SUP','Phòng vận hành');

-- Cập nhật user hiện tại
UPDATE `users` SET 
    `password` = '$2a$10$phIThd.OoOLpbeu86x.xseUy2p69syVgD8z2SQODc2WHhrzickAcG', -- password: 123456
    `role` = 'SUP',
    `is_department_manager` = TRUE
WHERE `user_id` = 1;

-- Thêm thêm một số user mẫu
INSERT INTO `users` (`username`, `email`, `full_name`, `department_id`, `role`, `password`, `is_department_manager`, `created_at`, `updated_at`) VALUES
('manager_it', 'manager.it@company.com', 'Manager IT', 1, 'MANA', '$2a$10$phIThd.OoOLpbeu86x.xseUy2p69syVgD8z2SQODc2WHhrzickAcG', TRUE, NOW(), NOW()),
('employee_it', 'employee.it@company.com', 'Employee IT', 1, 'EMPL', '$2a$10$phIThd.OoOLpbeu86x.xseUy2p69syVgD8z2SQODc2WHhrzickAcG', FALSE, NOW(), NOW()),
('manager_sale', 'manager.sale@company.com', 'Manager Sale', 2, 'MANA', '$2a$10$phIThd.OoOLpbeu86x.xseUy2p69syVgD8z2SQODc2WHhrzickAcG', TRUE, NOW(), NOW()),
('employee_sale', 'employee.sale@company.com', 'Employee Sale', 2, 'EMPL', '$2a$10$phIThd.OoOLpbeu86x.xseUy2p69syVgD8z2SQODc2WHhrzickAcG', FALSE, NOW(), NOW());