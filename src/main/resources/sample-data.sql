USE `lesson9`;

INSERT
  INTO `books`
       (`title`, `author`)
VALUES ('Cool Book 1', 'Fun Author 1')
     , ('Weird Book 2', 'Eccentric Author 2')
     , ('Nice Book 3', 'Fun Author 1')
     , ('Very Old Book 4', 'Unknown');

INSERT
  INTO `copies`
       (`bookId`, `condition`)
VALUES (1, 3)
     , (1, 2)
     , (2, 1)
     , (3, 3)
     , (3, 3)
     , (3, 0)
     , (4, 0)
     , (4, 2)
     , (4, 1);

INSERT
  INTO `customers`
       (`name`)
VALUES ('Friendly Customer 1')
     , ('Notorious Customer 2')
     , ('Studious Customer 3')
     , ('Cheerful Customer 4');

INSERT
  INTO `loans`
       (`copyId`, `customerId`, `dateLoaned`, `dateDue`, `dateReturned`)
VALUES (8, 4, CURRENT_DATE() - INTERVAL 30 DAY, CURRENT_DATE() - INTERVAL 16 DAY, CURRENT_DATE() - INTERVAL 16 DAY)
     , (5, 3, CURRENT_DATE() - INTERVAL 25 DAY, CURRENT_DATE() - INTERVAL 11 DAY, CURRENT_DATE() - INTERVAL 15 DAY)
     , (4, 1, CURRENT_DATE() - INTERVAL 21 DAY, CURRENT_DATE() - INTERVAL 7 DAY, CURRENT_DATE() - INTERVAL 10 DAY)
     , (3, 2, CURRENT_DATE() - INTERVAL 20 DAY, CURRENT_DATE() - INTERVAL 6 DAY, NULL)
     , (8, 3, CURRENT_DATE() - INTERVAL 15 DAY, CURRENT_DATE() - INTERVAL 1 DAY, NULL)
     , (1, 1, CURRENT_DATE() - INTERVAL 10 DAY, CURRENT_DATE() + INTERVAL 4 DAY, NULL);
