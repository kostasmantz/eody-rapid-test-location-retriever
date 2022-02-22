db.createUser(
    {
        user: "eody-sites-user",
        pwd: "my-strong-pass",
        roles: [
            {
                role: "readWrite",
                db: "eody-sites"
            }
        ]
    }
)