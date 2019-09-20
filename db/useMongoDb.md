# MongoDB

## Install
Apple only (I think):
brew tap mongodb/brew
brew install mongodb-community@4.2

## Usage
start server
mongod --config /usr/local/etc/mongod.conf

open mongo shell
mongo

Restore dump
Download from https://static.openfoodfacts.org/data/openfoodfacts-mongodbdump.tar.gz and unzip
mongorestore -d openFoodFacts /Users/angeloaracri/projectSupermarket/project-supermarket/db/dump/off

## MongoDB Query language
display used db
db

Switch to off db (thats where mongorestore puts the data)
use off

List all collections
db.runCommand( { listCollections: 1 } );

db.products.find({}).limit(1)
db.products.find( { code: 4001518103487 } )
db.products.find( { product_name: "Soft Cake Orange" } ).image_url
