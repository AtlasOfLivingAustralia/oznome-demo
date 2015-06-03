oznome-demo
=========

Code for oznome demonstrator

## Information

Grails version required: 2.3.11

To run it locally unzip oznome-demo-local-config.zip in your root folder.

## Custom events for Region client UI
* **groupSelected**: triggered when a species group is selected. It includes the following data:
```
{
  group: '...'
}
```
* **subgroupSelected**: triggered when a species subgroup is selected. It includes the following data:
```
{
  group: '...',
  subgroup: '...'
}
```
* **subgroupSelected**: triggered when a species is selected. It includes the following data:
```
{
  group: '...',
  subgroup: '...',
  speciesName: '...'
}
```

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

## Species/Patent Search

Service: GET to [url_base]/[region_type]/[region_name]/patents
Optional parameter: summary=true

Example query: http://localhost:8080/oznome-demo/Indigenous%20protected%20areas/Deen%20Maar/patents
Example response
```
{

    "count": 161,
    "species": [
      {
        "name": "Mus musculus",
        "lsid": "urn:lsid:biodiversity.org.au:afd.taxon:107696b5-063c-4c09-a015-6edfdb6f4d52",
        "commonName": "House Mouse",
        "occurrenceCount": 1,
        "patentCount": 45,
        "patents": [
           {
              "applicationNumber": "2013202235",
              "title": "Use of Hsp70 as a regulator of enzymatic activity",
               "applicants": "Orphazyme ApS",
               "inventors": "Jensen, Thomas Kirkegaard; Jaattela, Marja Helena",
               "filingDate": "2013-04-02",
               "filingStatus": "ACCEPTED"

           },
           ...
        ]
    },
    ...
    {
       "name": "Acanthiza (Acanthiza) pusilla",
       "lsid": "urn:lsid:biodiversity.org.au:afd.taxon:7c747b85-9ef1-4a2c-839d-0efd7e794af4",
       "commonName": "Brown Thornbill",
       "occurrenceCount": 1,
       "patentCount": 0,
       "patents": [ ]

    },
    ...
}
```

Example query: http://localhost:8080/oznome-demo/Indigenous%20protected%20areas/Deen%20Maar/patents?summary=true
Example response
```
{

    "count": 161,
    "species": [
      {
        "name": "Mus musculus",
        "lsid": "urn:lsid:biodiversity.org.au:afd.taxon:107696b5-063c-4c09-a015-6edfdb6f4d52",
        "commonName": "House Mouse",
        "occurrenceCount": 1,
        "patentCount": 45
    },
    ...
    {
       "name": "Acanthiza (Acanthiza) pusilla",
       "lsid": "urn:lsid:biodiversity.org.au:afd.taxon:7c747b85-9ef1-4a2c-839d-0efd7e794af4",
       "commonName": "Brown Thornbill",
       "occurrenceCount": 1,
       "patentCount": 0,
    },
    ...
}
```
