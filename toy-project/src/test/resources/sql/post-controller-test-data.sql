insert into `users` (`id`, `email`, `nickname`, `address`, `certification_code`, `status`, `last_login_at`)
values (1, 'test@naver.com', 'test', 'Seoul', 'aaaaaaaa-aaa-aaaaaa-aaaaaaaa', 'ACTIVE', 0);
insert into `posts` (`id`, `content`, `created_at`, `modified_at`, `user_id`)
values (1, 'helloworld', 1678530673958, 0, 1);