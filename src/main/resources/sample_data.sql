-- Insert into editor (publisher)
INSERT INTO editor (id, name) VALUES (3, 'Global Tech Publications');
INSERT INTO editor (id, name) VALUES (4, 'Academic Press');
INSERT INTO editor (id, name) VALUES (5, 'Future Books Inc.');
INSERT INTO editor (id, name) VALUES (6, 'Nova Scientific');
INSERT INTO editor (id, name) VALUES (7, 'Digital Reads');
INSERT INTO editor (id, name) VALUES (8, 'Apex Publishing');
INSERT INTO editor (id, name) VALUES (9, 'Horizon Books');
INSERT INTO editor (id, name) VALUES (10, 'Pinnacle Press');
INSERT INTO editor (id, name) VALUES (11, 'Eon Publishing');
INSERT INTO editor (id, name) VALUES (12, 'Zenith Books');
INSERT INTO editor (id, name) VALUES (13, 'Vanguard Books');
INSERT INTO editor (id, name) VALUES (14, 'Legacy Press');
INSERT INTO editor (id, name) VALUES (15, 'Celestial Books');
INSERT INTO editor (id, name) VALUES (16, 'Terra Publishing');

-- Insert into book (con editor_id direttamente)
INSERT INTO book (id, title, sub_title, isbn, publication_date, genre, pages, summary, editor_id)
VALUES (3, 'Networked neural strategy', 'synthesize customized metrics', '9780785316371', '2022-11-05', 'AI', 412, 'Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.', 3);

INSERT INTO book (id, title, sub_title, isbn, publication_date, genre, pages, summary, editor_id)
VALUES (4, 'Enhanced multimedia interface', 'recontextualize strategic applications', '9780230278458', '2017-09-12', 'Technology', 287, 'Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris.', 4);

INSERT INTO book (id, title, sub_title, isbn, publication_date, genre, pages, summary, editor_id)
VALUES (5, 'Optimized data throughput', 'architect intuitive deliverables', '9781473689271', '2021-04-28', 'AI', 634, 'Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.', 5);

INSERT INTO book (id, title, sub_title, isbn, publication_date, genre, pages, summary, editor_id)
VALUES (6, 'Decentralized systems integration', 'facilitate scalable partnerships', '9781607436911', '2019-01-15', 'Technology', 389, 'Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.', 6);

INSERT INTO book (id, title, sub_title, isbn, publication_date, genre, pages, summary, editor_id)
VALUES (7, 'Algorithmic pattern analysis', 'empower efficient e-services', '9780810885720', '2023-03-01', 'AI', 510, 'Aenean quis ex condimentum, convallis sapien a, sollicitudin leo.', 7);

INSERT INTO book (id, title, sub_title, isbn, publication_date, genre, pages, summary, editor_id)
VALUES (8, 'Quantum cryptography in practice', 'implement secure protocols', '9780321765723', '2020-12-20', 'Technology', 450, 'Phasellus eu felis accumsan, iaculis lacus sit amet, finibus diam.', 8);

INSERT INTO book (id, title, sub_title, isbn, publication_date, genre, pages, summary, editor_id)
VALUES (9, 'The Future of Robotics', 'Exploring advancements in automation', '9780470909583', '2024-01-01', 'Robotics', 670, 'This book delves into the latest trends in robotics.', 9);

INSERT INTO book (id, title, sub_title, isbn, publication_date, genre, pages, summary, editor_id)
VALUES (10, 'Biotechnology breakthroughs', 'Advances in genetic engineering', '9781107015534', '2022-05-15', 'Biotech', 580, 'An overview of the recent developments in the field of biotechnology.', 10);

INSERT INTO book (id, title, sub_title, isbn, publication_date, genre, pages, summary, editor_id)
VALUES (11, 'Advanced material science', 'Synthesis and characterization', '9780387955911', '2021-02-28', 'Materials', 430, 'Detailed analysis of various advanced materials.', 11);

INSERT INTO book (id, title, sub_title, isbn, publication_date, genre, pages, summary, editor_id)
VALUES (12, 'Astrophysics fundamentals', 'Exploring the cosmos', '9780521176560', '2019-08-10', 'Physics', 720, 'This book explores the vastness of the universe.', 12);

INSERT INTO book (id, title, sub_title, isbn, publication_date, genre, pages, summary, editor_id)
VALUES (13, 'Environmental engineering', 'Sustainability and renewable energy', '9780470873587', '2023-09-30', 'Environment', 390, 'A practical approach to environmental engineering challenges.', 13);

INSERT INTO book (id, title, sub_title, isbn, publication_date, genre, pages, summary, editor_id)
VALUES (14, 'Introduction to Data Science', 'Practical Data Analysis', '9781501582327', '2020-07-20', 'Data Science', 450, 'Comprehensive introduction to data science techniques', 14);

INSERT INTO book (id, title, sub_title, isbn, publication_date, genre, pages, summary, editor_id)
VALUES (15, 'The Art of Software Engineering', 'Best Practices for Development', '9780321967974', '2021-03-15', 'Software Eng', 650, 'A practical guide to software development', 15);

INSERT INTO book (id, title, sub_title, isbn, publication_date, genre, pages, summary, editor_id)
VALUES (16, 'Machine Learning Algorithms', 'Theory and Implementation', '9780596805190', '2022-06-01', 'Machine Learning', 500, 'In-depth analysis of machine learning algorithms', 16);


-- Insert into book_languages
INSERT INTO book_languages (book_id, language) VALUES (3, 'eng');
INSERT INTO book_languages (book_id, language) VALUES (3, 'deu');
INSERT INTO book_languages (book_id, language) VALUES (4, 'eng');
INSERT INTO book_languages (book_id, language) VALUES (4, 'spa');
INSERT INTO book_languages (book_id, language) VALUES (5, 'ita');
INSERT INTO book_languages (book_id, language) VALUES (5, 'eng');
INSERT INTO book_languages (book_id, language) VALUES (6, 'fre');
INSERT INTO book_languages (book_id, language) VALUES (6, 'eng');
INSERT INTO book_languages (book_id, language) VALUES (7, 'eng');
INSERT INTO book_languages (book_id, language) VALUES (7, 'jpn');
INSERT INTO book_languages (book_id, language) VALUES (8, 'eng');
INSERT INTO book_languages (book_id, language) VALUES (8, 'rus');
INSERT INTO book_languages (book_id, language) VALUES (9, 'eng');
INSERT INTO book_languages (book_id, language) VALUES (10, 'eng');
INSERT INTO book_languages (book_id, language) VALUES (11, 'eng');
INSERT INTO book_languages (book_id, language) VALUES (12, 'eng');
INSERT INTO book_languages (book_id, language) VALUES (13, 'eng');
INSERT INTO book_languages (book_id, language) VALUES (14, 'eng');
INSERT INTO book_languages (book_id, language) VALUES (15, 'eng');
INSERT INTO book_languages (book_id, language) VALUES (16, 'eng');


-- Insert into book_formats
INSERT INTO book_formats (book_id, format) VALUES (3, 'EPUB');
INSERT INTO book_formats (book_id, format) VALUES (3, 'MOBI');
INSERT INTO book_formats (book_id, format) VALUES (4, 'PDF');
INSERT INTO book_formats (book_id, format) VALUES (4, 'MOBI');
INSERT INTO book_formats (book_id, format) VALUES (5, 'EPUB');
INSERT INTO book_formats (book_id, format) VALUES (5, 'PDF');
INSERT INTO book_formats (book_id, format) VALUES (6, 'MOBI');
INSERT INTO book_formats (book_id, format) VALUES (6, 'AZW3');
INSERT INTO book_formats (book_id, format) VALUES (7, 'EPUB');
INSERT INTO book_formats (book_id, format) VALUES (7, 'AZW3');
INSERT INTO book_formats (book_id, format) VALUES (8, 'PDF');
INSERT INTO book_formats (book_id, format) VALUES (8, 'EPUB');
INSERT INTO book_formats (book_id, format) VALUES (9, 'EPUB');
INSERT INTO book_formats (book_id, format) VALUES (9, 'PDF');
INSERT INTO book_formats (book_id, format) VALUES (10, 'PDF');
INSERT INTO book_formats (book_id, format) VALUES (10, 'MOBI');
INSERT INTO book_formats (book_id, format) VALUES (11, 'EPUB');
INSERT INTO book_formats (book_id, format) VALUES (12, 'EPUB');
INSERT INTO book_formats (book_id, format) VALUES (13, 'MOBI');
INSERT INTO book_formats (book_id, format) VALUES (14, 'PDF');
INSERT INTO book_formats (book_id, format) VALUES (15, 'MOBI');
INSERT INTO book_formats (book_id, format) VALUES (16, 'EPUB');


-- Insert into book_keywords
INSERT INTO book_keywords (book_id, keyword) VALUES (3, 'neural-networks');
INSERT INTO book_keywords (book_id, keyword) VALUES (3, 'big-data');
INSERT INTO book_keywords (book_id, keyword) VALUES (4, 'user-interface');
INSERT INTO book_keywords (book_id, keyword) VALUES (4, 'system-design');
INSERT INTO book_keywords (book_id, keyword) VALUES (5, 'deep-learning');
INSERT INTO book_keywords (book_id, keyword) VALUES (5, 'data-mining');
INSERT INTO book_keywords (book_id, keyword) VALUES (6, 'blockchain');
INSERT INTO book_keywords (book_id, keyword) VALUES (6, 'distributed-systems');
INSERT INTO book_keywords (book_id, keyword) VALUES (7, 'image-recognition');
INSERT INTO book_keywords (book_id, keyword) VALUES (7, 'natural-language-processing');
INSERT INTO book_keywords (book_id, keyword) VALUES (8, 'quantum-security');
INSERT INTO book_keywords (book_id, keyword) VALUES (8, 'cryptography');
INSERT INTO book_keywords (book_id, keyword) VALUES (9, 'autonomous-systems');
INSERT INTO book_keywords (book_id, keyword) VALUES (9, 'industrial-robotics');
INSERT INTO book_keywords (book_id, keyword) VALUES (10, 'genetic-engineering');
INSERT INTO book_keywords (book_id, keyword) VALUES (10, 'biomedical-technology');
INSERT INTO book_keywords (book_id, keyword) VALUES (11, 'nanomaterials');
INSERT INTO book_keywords (book_id, keyword) VALUES (11, 'composites');
INSERT INTO book_keywords (book_id, keyword) VALUES (12, 'galaxies');
INSERT INTO book_keywords (book_id, keyword) VALUES (12, 'black-holes');
INSERT INTO book_keywords (book_id, keyword) VALUES (13, 'renewable-energy');
INSERT INTO book_keywords (book_id, keyword) VALUES (13, 'sustainability');
INSERT INTO book_keywords (book_id, keyword) VALUES (14, 'statistics');
INSERT INTO book_keywords (book_id, keyword) VALUES (14, 'data-visualization');
INSERT INTO book_keywords (book_id, keyword) VALUES (15, 'agile-development');
INSERT INTO book_keywords (book_id, keyword) VALUES (15, 'software-testing');
INSERT INTO book_keywords (book_id, keyword) VALUES (16, 'regression-models');
INSERT INTO book_keywords (book_id, keyword) VALUES (16, 'clustering-algorithms');

-- Insert into author
INSERT INTO author (id, first_name, last_name, sex, birth_date)
VALUES (3, 'Bob', 'Williams', 'M', '1968-11-01');
INSERT INTO author (id, first_name, last_name, sex, birth_date)
VALUES (4, 'Emily', 'Brown', 'F', '1990-02-14');
INSERT INTO author (id, first_name, last_name, sex, birth_date)
VALUES (5, 'David', 'Jones', 'M', '1979-07-29');
INSERT INTO author (id, first_name, last_name, sex, birth_date)
VALUES (6, 'Sarah', 'Miller', 'F', '1985-12-05');
INSERT INTO author (id, first_name, last_name, sex, birth_date)
VALUES (7, 'Michael', 'Davis', 'M', '1970-09-18');
INSERT INTO author (id, first_name, last_name, sex, birth_date)
VALUES (8, 'Jessica', 'Wilson', 'F', '1992-03-22');
INSERT INTO author (id, first_name, last_name, sex, birth_date)
VALUES (9, 'Christopher', 'Garcia', 'M', '1981-05-10');
INSERT INTO author (id, first_name, last_name, sex, birth_date)
VALUES (10, 'Ashley', 'Rodriguez', 'F', '1987-08-03');
INSERT INTO author (id, first_name, last_name, sex, birth_date)
VALUES (11, 'Matthew', 'Martinez', 'M', '1994-04-21');
INSERT INTO author (id, first_name, last_name, sex, birth_date)
VALUES (12, 'Amanda', 'Anderson', 'F', '1973-01-07');
INSERT INTO author (id, first_name, last_name, sex, birth_date)
VALUES (13, 'William', 'Taylor', 'M', '1989-06-15');
INSERT INTO author (id, first_name, last_name, sex, birth_date)
VALUES (14, 'Lauren', 'Thomas', 'F', '1977-10-20');
INSERT INTO author (id, first_name, last_name, sex, birth_date)
VALUES (15, 'Andrew', 'Jackson', 'M', '1984-09-28');
INSERT INTO author (id, first_name, last_name, sex, birth_date)
VALUES (16, 'Stephanie', 'White', 'F', '1998-02-09');
-- Insert into book_authors (relationship)
INSERT INTO book_authors (book_id, author_id) VALUES (3, 3);
INSERT INTO book_authors (book_id, author_id) VALUES (3, 4);
INSERT INTO book_authors (book_id, author_id) VALUES (4, 5);
INSERT INTO book_authors (book_id, author_id) VALUES (4, 6);
INSERT INTO book_authors (book_id, author_id) VALUES (5, 7);
INSERT INTO book_authors (book_id, author_id) VALUES (5, 8);
INSERT INTO book_authors (book_id, author_id) VALUES (6, 9);
INSERT INTO book_authors (book_id, author_id) VALUES (6, 10);
INSERT INTO book_authors (book_id, author_id) VALUES (7, 11);
INSERT INTO book_authors (book_id, author_id) VALUES (7, 12);
INSERT INTO book_authors (book_id, author_id) VALUES (8, 13);
INSERT INTO book_authors (book_id, author_id) VALUES (8, 14);
INSERT INTO book_authors (book_id, author_id) VALUES (9, 3);
INSERT INTO book_authors (book_id, author_id) VALUES (9, 16);
INSERT INTO book_authors (book_id, author_id) VALUES (10, 4);
INSERT INTO book_authors (book_id, author_id) VALUES (10, 15);
INSERT INTO book_authors (book_id, author_id) VALUES (11, 5);
INSERT INTO book_authors (book_id, author_id) VALUES (11, 14);
INSERT INTO book_authors (book_id, author_id) VALUES (12, 6);
INSERT INTO book_authors (book_id, author_id) VALUES (12, 13);
INSERT INTO book_authors (book_id, author_id) VALUES (13, 7);
INSERT INTO book_authors (book_id, author_id) VALUES (13, 12);
INSERT INTO book_authors (book_id, author_id) VALUES (14, 8);
INSERT INTO book_authors (book_id, author_id) VALUES (14, 11);
INSERT INTO book_authors (book_id, author_id) VALUES (15, 9);
INSERT INTO book_authors (book_id, author_id) VALUES (15, 10);

-- Alter sequence
ALTER SEQUENCE AUTHOR_SEQ RESTART WITH 50;
ALTER SEQUENCE BOOK_SEQ RESTART WITH 50;
ALTER SEQUENCE EDITOR_SEQ RESTART WITH 50;
