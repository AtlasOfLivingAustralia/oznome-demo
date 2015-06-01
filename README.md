oznome-demo
=========

Code for oznome demonstrator

## Information

Grails version required: 2.3.11

To run it locally unzip oznome-demo-local-config.zip in your root folder.


## Patent search
Requires MongoDB to be installed

Service: POST to [url_base]/patents

Sample request
```
["Acacia sutherlandii", "Acacia melanoxylon"]
```

Sample response:
```
{
  "Acacia sutherlandii": [],
  "Acacia melanoxylon": [
    {
      "applicationNumber": "2015900524",
      "title": "Table tennis racket, 100% Australian made using Tasmanian Blackwood (Acacia Melanoxylon) wood for the blade with balsa handle",
      "applicants": "McFadden, Ronald Edward MR",
      "inventors": "McFadden, Ronald Edward",
      "filingDate": "2015-01-26",
      "filingStatus": "FILED"
    },
    {
      "applicationNumber": "2015100081",
      "title": "Table tennis racket, 100% Australian made using Tasmanian Blackwood (Acacia Melanoxylon) wood for the blade with balsa handle",
      "applicants": "McFadden, Ronald Edward MR",
      "inventors": "McFadden, Ronald Edward",
      "filingDate": "2015-01-26",
      "filingStatus": "CONVERTED"
    }
  ]
}
```