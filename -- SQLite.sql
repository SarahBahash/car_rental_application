-- SQLite
UPDATE cars SET image_path = 'images/' || SUBSTR(image_path, INSTR(image_path, 'images/') + 7);

