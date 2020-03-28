-- :name create-user! :! :n
-- :doc creates a new user record
INSERT INTO users
    (id, first_name, last_name, email, pass)
VALUES (:id, :first_name, :last_name, :email, :pass);

-- :name update-user! :! :n
-- :doc updates an existing user record
UPDATE users
SET first_name = :first_name,
    last_name  = :last_name,
    email      = :email
WHERE id = :id;

-- :name get-user :? :1
-- :doc retrieves a user record given the id
SELECT *
FROM users
WHERE id = :id;

-- :name delete-user! :! :n
-- :doc deletes a user record given the id
DELETE
FROM users
WHERE id = :id;

-- :name get-access-token :? :1
-- :doc retrieves the most recent access token
SELECT *
FROM td_auth
WHERE token_type = 'access_token';

-- :name get-refresh-token :? :1
-- :doc retrieves the most recent refresh token
SELECT *
FROM td_auth
WHERE token_type = 'refresh_token';

-- :name update-token :! :n
-- :doc update or insert a token
INSERT INTO td_auth (token_type, token, expiry)
VALUES (:token_type, :token, :expiry)
ON CONFLICT (token_type)
    DO UPDATE SET token  = excluded.token,
                  expiry = excluded.expiry
