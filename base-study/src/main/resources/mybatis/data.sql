INSERT
    t_user
values
       (1, 'user1', now()),
       (2, 'user2', now()),
       (3, 'user3', now());

INSERT
    t_order
values
       (1, 1),
       (2, 1),
       (3, 2),
       (4, 2),
       (5, 2);

INSERT
    t_commodity
values
       (1, 'commodity1'),
       (2, 'commodity2'),
       (3, 'commodity3'),
       (4, 'commodity4');

INSERT
    t_order_commodity
values
       (1, 1, 5),
       (1, 3, 3),
       (2, 2, 2),
       (3, 1, 1),
       (3, 2, 2),
       (3, 3, 3),
       (4, 1, 1),
       (5, 1, 1);