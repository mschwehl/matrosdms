# matrosdms
Document Management System (DMS) based on Graph Theory and Eclipse Rich Client (RCP)

# The idea

in the real world you deal with entities like car, family members, dealers and companies.

# Why not rebuild this network in your DMS ? 

Every business-paper has two partners, the issuer and the reciever, and an entity that is subject of the paper. The paper itself have a type like invoice, contract and so on

example: Your Car

* Reciever:    Mister Smith
* Issuer:      CarSeller PETE
* Object:      BMW XDrive

 Type of Paper:   contract

So you take the tags (you, Mr. Smith) and CarSeller PETE, the BMW and save this constellation to a new context (a virual folder).
In this context you can save your documents.

Your documents (e.g. pdf-files) can applied with metadata (bill, $1000 or taxrelevance for 2017) you can define yourself

When you need a timeline of all documents for your bmw just klick on it in the application and you have the complete timeline!

Same for the carseller PETE and so on

You can define also a hierarcy in the tags

# reseller  -> amazon, ebay

you can query for reseller an all contexts (and therefore all documents) are listed for amazon and ebay
 
 You deal with object of your daily live and therefore you will find them quick and intuitive.
 
# On important note: Of course you shall never destroy your letters physically, keep them on a save place

In this dms you just need to decide if you want to keep the original or destroy it after scanning.

All originals get a new number and you can place it on top of a stack. You find your documents in the DMS, get the number, go to the stack and get them. simple as that!

---

# Security is one big concern when you scan your personal documents

Thats why this DMS is not build as Online/Cloud/Webservice.

* I want my scans on my computer only
* I want my papers crypted with standardsoftware i decide for me (like 7zip)
* I want to open them in 20 years without this software

## This DMS respects this requirements

* all documents are stored i a singe folder for easy backup
* all documents are encrypted with your favorite cryting software (like 7zip)
* source on github, no vendor-lockin
* easy export of your documents
* you own all your passwords and accounts, no secrets for the advanced users

## Source 
using Eclipse E4 / Tycho / Eclipse Oxygen

### Screenshots
## Tagging perspective
![the tagging perspective](/docs/en/images/inbox.jpg?raw=true "Inbox")

## Add Document view
![add a documen](/docs/en/images/add_document.jpg?raw=true "add Document")

# Licence

* not descition yet, for commercial use contact me. 
* no warrenty

__contribution is welcome__

## Status: currently moving the code to github, take a look at the betaversion :-)
