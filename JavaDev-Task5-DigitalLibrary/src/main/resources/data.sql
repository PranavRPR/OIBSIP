INSERT OR IGNORE INTO users (username, password, role) VALUES ('admin', 'admin123', 'ADMIN');
INSERT OR IGNORE INTO users (username, password, role) VALUES ('student', 'student123', 'USER');

INSERT OR IGNORE INTO books (title, author, category, quantity, available) VALUES ('Java Programming', 'James Gosling', 'Technology', 5, 5);
INSERT OR IGNORE INTO books (title, author, category, quantity, available) VALUES ('Clean Code', 'Robert Martin', 'Technology', 3, 3);
INSERT OR IGNORE INTO books (title, author, category, quantity, available) VALUES ('The Great Gatsby', 'F. Scott Fitzgerald', 'Fiction', 2, 2);