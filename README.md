[![Build Status](https://travis-ci.org/hmrc/reference-checker.svg)](https://travis-ci.org/hmrc/reference-checker)

# Reference Checker

Library to validate the tax references.

## Checkers

The following list of checkers are available. Each checker returns `true` or `false` depending if the passed reference is valid.

#### Self Assessment UTR
 
    SelfAssessmentReferenceChecker.isValid("2234567890K)
 
#### Corporation Tax UTR

    CorporationTaxReferenceChecker.isValid("2234567890")

#### VAT reference

    VatReferenceChecker.isValid("101747696")

#### ePAYE reference

This reference checker can be also used to validate P11D (Class 1A National Insurance) references.

    EpayeReferenceChecker.isValid("123PW12345678")

#### SDLT reference

    SdltReferenceChecker.isValid("123456789MA")

#### Other Taxes (ePAYE penalties, ePAYE settlement agreements, CIS penalties, and other references that start with 'X')

    OtherTaxReferenceChecker.isValid("XA012345678901")
