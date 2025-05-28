-- phpMyAdmin SQL Dump
-- version 4.8.5
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th3 26, 2025 lúc 02:18 PM
-- Phiên bản máy phục vụ: 10.1.38-MariaDB
-- Phiên bản PHP: 5.6.40

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `employee_assessment`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `assessment_results`
--

CREATE TABLE `assessment_results` (
                                      `result_id` int(11) NOT NULL,
                                      `assessment_period` varchar(255) DEFAULT NULL,
                                      `created_at` datetime(6) DEFAULT NULL,
                                      `status` enum('DRAFT','FINALIZED','SUBMITTED') DEFAULT NULL,
                                      `total_score` decimal(10,2) DEFAULT NULL,
                                      `updated_at` datetime(6) DEFAULT NULL,
                                      `assessed_user_id` int(11) NOT NULL,
                                      `assessor_user_id` int(11) NOT NULL,
                                      `template_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `assessment_result_details`
--

CREATE TABLE `assessment_result_details` (
                                             `detail_id` int(11) NOT NULL,
                                             `comments` text,
                                             `score` int(11) NOT NULL,
                                             `criteria_id` int(11) NOT NULL,
                                             `result_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `assessment_templates`
--

CREATE TABLE `assessment_templates` (
                                        `template_id` int(11) NOT NULL,
                                        `created_at` datetime(6) DEFAULT NULL,
                                        `description` text,
                                        `is_active` bit(1) DEFAULT NULL,
                                        `template_name` varchar(255) NOT NULL,
                                        `updated_at` datetime(6) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Đang đổ dữ liệu cho bảng `assessment_templates`
--

INSERT INTO `assessment_templates` (`template_id`, `created_at`, `description`, `is_active`, `template_name`, `updated_at`) VALUES
                                                                                                                                (5, '2025-03-26 09:14:13.000000', 'Đánh giá chi tiết nhân viên trong tháng 2', b'1', 'Bảng đánh giá nhân viên tháng 2 edited', '2025-03-26 09:17:03.000000'),
                                                                                                                                (6, '2025-03-26 09:17:42.000000', 'Đánh giá chi tiết nhân viên trong tháng 2', b'1', 'Bảng đánh giá nhân viên tháng 2 edited (Copy)', '2025-03-26 09:17:42.000000');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `criteria_bank`
--

CREATE TABLE `criteria_bank` (
                                 `criteria_id` int(11) NOT NULL,
                                 `category` varchar(255) DEFAULT NULL,
                                 `created_at` datetime(6) DEFAULT NULL,
                                 `criteria_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                                 `default_max_score` int(11) NOT NULL,
                                 `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
                                 `is_active` bit(1) DEFAULT NULL,
                                 `updated_at` datetime(6) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Đang đổ dữ liệu cho bảng `criteria_bank`
--

INSERT INTO `criteria_bank` (`criteria_id`, `category`, `created_at`, `criteria_name`, `default_max_score`, `description`, `is_active`, `updated_at`) VALUES
                                                                                                                                                          (1, 'Năng lực', '2025-03-26 08:23:55.000000', 'Kỹ năng chuyên môn', 10, 'Đánh giá mức độ thành thạo và kiến thức chuyên môn liên quan đến công việc.', b'1', '2025-03-26 08:23:55.000000'),
                                                                                                                                                          (2, 'Năng lực', '2025-03-26 08:24:14.000000', 'Kỹ năng sử dụng công cụ', 10, 'Khả năng sử dụng các công cụ và phần mềm hỗ trợ công việc một cách hiệu quả.', b'1', '2025-03-26 08:24:14.000000'),
                                                                                                                                                          (3, 'Thái độ', '2025-03-26 08:24:21.000000', 'Tính kỷ luật', 10, 'Tuân thủ nội quy, quy định của công ty và thực hiện công việc đúng thời hạn.', b'1', '2025-03-26 08:24:21.000000'),
                                                                                                                                                          (4, 'Thái độ', '2025-03-26 08:24:28.000000', 'Tính chuyên cần', 10, 'Sự chăm chỉ, đi làm đầy đủ và đúng giờ.', b'1', '2025-03-26 08:24:28.000000'),
                                                                                                                                                          (5, 'Hiệu suất', '2025-03-26 08:24:35.000000', 'Tiến độ làm việc', 10, 'Khả năng hoàn thành công việc theo đúng kế hoạch và thời gian đã đề ra.', b'1', '2025-03-26 08:24:35.000000'),
                                                                                                                                                          (6, 'Hiệu suất', '2025-03-26 08:24:42.000000', 'Chất lượng công việc', 10, 'Đánh giá mức độ chính xác, hiệu quả và chất lượng của công việc được thực hiện.', b'1', '2025-03-26 08:24:42.000000'),
                                                                                                                                                          (7, 'Kỹ năng mềm', '2025-03-26 08:24:49.000000', 'Khả năng làm việc nhóm', 10, 'Khả năng hợp tác, giao tiếp và làm việc hiệu quả trong nhóm.', b'1', '2025-03-26 08:24:49.000000'),
                                                                                                                                                          (8, 'Kỹ năng mềm', '2025-03-26 08:24:56.000000', 'Khả năng giao tiếp', 10, 'Khả năng truyền đạt thông tin rõ ràng, lắng nghe và phản hồi hiệu quả.', b'1', '2025-03-26 08:24:56.000000'),
                                                                                                                                                          (9, 'Kỹ năng mềm', '2025-03-26 08:25:04.000000', 'Khả năng sáng tạo', 10, 'Khả năng đưa ra ý tưởng mới, giải pháp sáng tạo trong công việc.', b'1', '2025-03-26 08:25:04.000000'),
                                                                                                                                                          (11, 'Thái độ', '2025-03-26 08:26:01.000000', 'Tính trung thực', 10, 'Sự chân thành, minh bạch và độ đáng tin cậy trong công việc.', b'1', '2025-03-26 08:26:41.000000');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `departments`
--

CREATE TABLE `departments` (
                               `department_id` int(11) NOT NULL,
                               `created_at` datetime(6) DEFAULT NULL,
                               `department_name` varchar(255) NOT NULL,
                               `description` text,
                               `updated_at` datetime(6) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Đang đổ dữ liệu cho bảng `departments`
--

INSERT INTO `departments` (`department_id`, `created_at`, `department_name`, `description`, `updated_at`) VALUES
                                                                                                              (1, NULL, 'IT', 'SE  WEB MOBILE', NULL),
                                                                                                              (2, NULL, 'Sale', 'Phòng sale', NULL);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `flyway_schema_history`
--

CREATE TABLE `flyway_schema_history` (
                                         `installed_rank` int(11) NOT NULL,
                                         `version` varchar(50) DEFAULT NULL,
                                         `description` varchar(200) NOT NULL,
                                         `type` varchar(20) NOT NULL,
                                         `script` varchar(1000) NOT NULL,
                                         `checksum` int(11) DEFAULT NULL,
                                         `installed_by` varchar(100) NOT NULL,
                                         `installed_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                         `execution_time` int(11) NOT NULL,
                                         `success` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `template_criteria_mapping`
--

CREATE TABLE `template_criteria_mapping` (
                                             `mapping_id` int(11) NOT NULL,
                                             `criteria_order` int(11) DEFAULT NULL,
                                             `max_score` int(11) NOT NULL,
                                             `criteria_id` int(11) NOT NULL,
                                             `template_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Đang đổ dữ liệu cho bảng `template_criteria_mapping`
--

INSERT INTO `template_criteria_mapping` (`mapping_id`, `criteria_order`, `max_score`, `criteria_id`, `template_id`) VALUES
                                                                                                                        (19, 0, 10, 2, 5),
                                                                                                                        (20, 1, 10, 3, 5),
                                                                                                                        (21, 2, 10, 4, 5),
                                                                                                                        (22, 3, 10, 5, 5),
                                                                                                                        (23, 4, 10, 6, 5),
                                                                                                                        (24, 5, 10, 7, 5),
                                                                                                                        (25, 6, 10, 8, 5),
                                                                                                                        (26, 7, 10, 9, 5),
                                                                                                                        (29, 0, 10, 2, 6),
                                                                                                                        (30, 1, 10, 3, 6),
                                                                                                                        (31, 2, 10, 4, 6),
                                                                                                                        (32, 3, 10, 5, 6),
                                                                                                                        (33, 4, 10, 6, 6),
                                                                                                                        (34, 5, 10, 7, 6),
                                                                                                                        (35, 6, 10, 8, 6),
                                                                                                                        (36, 7, 10, 9, 6),
                                                                                                                        (39, 8, 10, 1, 5),
                                                                                                                        (40, 9, 10, 11, 5);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `users`
--

CREATE TABLE `users` (
                         `user_id` int(11) NOT NULL,
                         `created_at` datetime(6) DEFAULT NULL,
                         `email` varchar(255) NOT NULL,
                         `full_name` varchar(255) NOT NULL,
                         `role` enum('EMPLOYEE','SUPERVISOR') DEFAULT NULL,
                         `updated_at` datetime(6) DEFAULT NULL,
                         `username` varchar(255) NOT NULL,
                         `department_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Đang đổ dữ liệu cho bảng `users`
--

INSERT INTO `users` (`user_id`, `created_at`, `email`, `full_name`, `role`, `updated_at`, `username`, `department_id`) VALUES
    (1, '2025-03-26 13:15:23.000000', 'huy@gmail.com', 'Hoang Huy', 'EMPLOYEE', '2025-03-26 13:15:23.000000', 'hoanghuy', 1);

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `assessment_results`
--
ALTER TABLE `assessment_results`
    ADD PRIMARY KEY (`result_id`),
  ADD KEY `FKshnp1lwxpixj3arwukcawx9i4` (`assessed_user_id`),
  ADD KEY `FKlfe5xwq9d8jblrkbxjr6m5t9v` (`assessor_user_id`),
  ADD KEY `FK8uuwdw63xmigpss11br7lu0y9` (`template_id`);

--
-- Chỉ mục cho bảng `assessment_result_details`
--
ALTER TABLE `assessment_result_details`
    ADD PRIMARY KEY (`detail_id`),
  ADD KEY `FK1f1djx0741ep3fjtb308ix8dk` (`criteria_id`),
  ADD KEY `FKpxxnp9xlev10n5lhrdxayg3q` (`result_id`);

--
-- Chỉ mục cho bảng `assessment_templates`
--
ALTER TABLE `assessment_templates`
    ADD PRIMARY KEY (`template_id`);

--
-- Chỉ mục cho bảng `criteria_bank`
--
ALTER TABLE `criteria_bank`
    ADD PRIMARY KEY (`criteria_id`);

--
-- Chỉ mục cho bảng `departments`
--
ALTER TABLE `departments`
    ADD PRIMARY KEY (`department_id`);

--
-- Chỉ mục cho bảng `flyway_schema_history`
--
ALTER TABLE `flyway_schema_history`
    ADD PRIMARY KEY (`installed_rank`),
  ADD KEY `flyway_schema_history_s_idx` (`success`);

--
-- Chỉ mục cho bảng `template_criteria_mapping`
--
ALTER TABLE `template_criteria_mapping`
    ADD PRIMARY KEY (`mapping_id`),
  ADD UNIQUE KEY `UKmndmsiidj9po8u01w1byiddq6` (`template_id`,`criteria_id`),
  ADD KEY `FKk8sb4i5jyp8ekwxqtowm8yra3` (`criteria_id`);

--
-- Chỉ mục cho bảng `users`
--
ALTER TABLE `users`
    ADD PRIMARY KEY (`user_id`),
  ADD KEY `FKsbg59w8q63i0oo53rlgvlcnjq` (`department_id`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `assessment_results`
--
ALTER TABLE `assessment_results`
    MODIFY `result_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `assessment_result_details`
--
ALTER TABLE `assessment_result_details`
    MODIFY `detail_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `assessment_templates`
--
ALTER TABLE `assessment_templates`
    MODIFY `template_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT cho bảng `criteria_bank`
--
ALTER TABLE `criteria_bank`
    MODIFY `criteria_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT cho bảng `departments`
--
ALTER TABLE `departments`
    MODIFY `department_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT cho bảng `template_criteria_mapping`
--
ALTER TABLE `template_criteria_mapping`
    MODIFY `mapping_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=41;

--
-- AUTO_INCREMENT cho bảng `users`
--
ALTER TABLE `users`
    MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `assessment_results`
--
ALTER TABLE `assessment_results`
    ADD CONSTRAINT `FK8uuwdw63xmigpss11br7lu0y9` FOREIGN KEY (`template_id`) REFERENCES `assessment_templates` (`template_id`),
  ADD CONSTRAINT `FKlfe5xwq9d8jblrkbxjr6m5t9v` FOREIGN KEY (`assessor_user_id`) REFERENCES `users` (`user_id`),
  ADD CONSTRAINT `FKshnp1lwxpixj3arwukcawx9i4` FOREIGN KEY (`assessed_user_id`) REFERENCES `users` (`user_id`);

--
-- Các ràng buộc cho bảng `assessment_result_details`
--
ALTER TABLE `assessment_result_details`
    ADD CONSTRAINT `FK1f1djx0741ep3fjtb308ix8dk` FOREIGN KEY (`criteria_id`) REFERENCES `criteria_bank` (`criteria_id`),
  ADD CONSTRAINT `FKpxxnp9xlev10n5lhrdxayg3q` FOREIGN KEY (`result_id`) REFERENCES `assessment_results` (`result_id`);

--
-- Các ràng buộc cho bảng `template_criteria_mapping`
--
ALTER TABLE `template_criteria_mapping`
    ADD CONSTRAINT `FKcj8o8pk9us2ob8ur3hei3hv4j` FOREIGN KEY (`template_id`) REFERENCES `assessment_templates` (`template_id`),
  ADD CONSTRAINT `FKk8sb4i5jyp8ekwxqtowm8yra3` FOREIGN KEY (`criteria_id`) REFERENCES `criteria_bank` (`criteria_id`);

--
-- Các ràng buộc cho bảng `users`
--
ALTER TABLE `users`
    ADD CONSTRAINT `FKsbg59w8q63i0oo53rlgvlcnjq` FOREIGN KEY (`department_id`) REFERENCES `departments` (`department_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
