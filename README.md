# CosmoDict
Dictionary of Frequent Terms Translated to All Languages Flat

The dictionary is a multi-contributions project created to provide a list of common terms translated to many languages, like this (table):

term  type  definition          english german  french  italian spanish russian zh-TW ...(etc)

be    verb  to exist or live    be      sein    être    essere  ser     быть    是

man  noun  person              man     Mann    homme   uomo    hombre  ...

(etc)

The data is useful to learn many languages at once, translate terms from language to language, and statistics.

The contributors are intended to be proficient language speakers and edit the data adding terms, translations, phrases and (language-free) images, sound files with pronunciation, etc.

Speakers from all the world are welcome to contribute and fill the data with ease.

The initial data goals are near 1000 verbs, 2000 nouns, 1000 adjs and 1000 other including numerals, prepositions, pronouns, etc.


#How to contribute

The dictionary is written in xml, UTF-8 charset.

##Dictionary

The root element is "dictionary".
        
        <?xml version="1.0" encoding="UTF-8"?>
        <dictionary>...</dictionary>

Other elements are:

##Term(s)

term elements are included into the terms element of dictionary.

A term is a text written in english (case insensitive written with chars [a-z][0-9]'-_ only) to index the definitions. 

        <dictionary>
          <terms>
            <term txt="be" priority="3">...</term>
          </terms>
        </dictionary>

##Types
type elements are included into the types element of dictionary.

        <dictionary>
          <types>
            <type id="noun"/>
            <type id="verb"/>
            <type id="adj"/>
            <type id="conj" name="conjunction"/>
            <type id="prep" name="preposition"/>
            <type id="art" name="article"/>
            <type id="pnoun" name="pronoun"/>
            <type id="num" name="numeral"/>
            <type id="prop" name="proper name"/>
            <type id="phrase" />
            <type id="null"/>
          </types>
        </dictionary>

##Language(s)

Include the supported languages by its ISO 693 2 letter code.
        <dictionary>
          <languages>
            <lang id="en" name="English"/>
            <lang id="it" name="Italiano" />
          </languages>
        </dictionary>

##Definition

Definitions are included into terms, only one definition per meaning.
Is a common meaning where all translations are related to.

        <term txt="work" priority="3">
          <definition type="verb">
            <value>to work</value>
            <image base64="true">binary base64 encoded text</image>
          </definition>
          <definition type="noun">
            <value>the work</value>
          </definition>  
        </term>

##Translation and Gender
Translations are included into definitions, only one translation per langage.

        <term txt="time" priority="3">
          <definition type="noun">
            <value>period era</value>
            <translation lang="de">
              <gender id="m" >der</gender>
              <gender id="f" priority="1">die</gender>
              <value>Zeit</value>
              <sound base64="true">binary base64 encoded text</sound>
            </translation>
            ...
            <translation...>...</translation>
          </definition>
        </term>

Genders are included into translations, the gender with priority 1 is used as the most common in case of ambiguity.

A gender is composed of the text that is added to the word, and an id to indicate (m=masculine,f=feminine,n=neutral), and a name (the description of the id, optional).

###Syllables, Chars, Pronunciation, Transliteration

The elements are included into translation element. If the element is of list type, its name is plural (with s) and contains multiple singular elements inside (without the s).

If the element is singular like pronunciation and transliteration it is written as is.

            <translation lang="zh-TW">
              <syllables count="2">
                <syllable>喜</syllable>
                <syllable>歡</syllable>
              </syllables>
              <chars>
                <char unicode="559C">
                  <value>喜</value>
                  <meaning>like, love, enjoy</meaning>
                  <pronunciation type="pinyin">Xǐ</pronunciation>
                </char>
                <char unicode="6B61">
                    <value>歡</value>
                    <meaning>happy, pleased, glad; joy; to enjoy</meaning>
                    <pronunciation type="pinyin">Xǐ</pronunciation>
                </char>
              </chars>
              <pronunciation type="pinyin">Xǐhuān</pronunciation>
              <transliteration type="pinyin">Xi3hua1n</transliteration>
            </translation>


##Conjugation,Mode,Time,Person
Conjugations are included into translation elements of definition of type "verb"

          <definition type="verb">
            <value>to be</value>
            <translation lang="en">
		          <conjugation>
          			<mode name="Infinitive">
          				<time name="Present">
          					<person name="I">am</person>
          					<person name="You">are</person>
          					...
          				</time>
          			</mode>
          		</conjugation>
          </translation>
          </definition>
	
##Phrase(s)
A phrase is a definition of type="phrase" related to the null term (withouth txt)

        <term priority="3">
          <definition type="phrase">
          </definition>
        </term>

##Xml schema file and verification

A xml schema file should be included in the project to verify the correct structure and data types of the elements described.
        
        schema.xsd
        
#Database file
The database file is
        
        cosmodict.xml
        
and all changes should be made to this file by the contributors.

Please verify that the previous changes did not delete all data before writing.
