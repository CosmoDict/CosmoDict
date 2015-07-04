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
            <term id="3" txt="be" priority="3">...</term>
          </terms>
        </dictionary>

A term must have a UNIQUE numeric id (by sequence).

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
            <type id="phrase"/>
            <type id="synonym"/>
            <type id="antonym"/>
            <type id="null"/>
          </types>
        </dictionary>

##Language(s)

Include the supported languages identified by its ISO 693 code (ISO 639-2 alpha-3 code). 
The content of each lang element is the language name written in the original language.

        <dictionary>
          <languages>
            <lang id="en" name="English" c2="en" c3="eng" />
            <lang id="ru" name="Russian" c2="ru" c3="rus"  >Русский язык</lang>
            <lang id="alb" name="Albanian" c2="sq" c3="alb" c31="sqi"></lang>
          </languages>
        </dictionary>

##Definition

Definitions are included into terms, only one definition per meaning.
Is a common meaning where all translations are related to.

        <term txt="work" priority="3">
          <definition type="verb" id="75">
            <value>to work</value>
            <image id="34">binary base64 encoded text</image>
          </definition>
          <definition type="noun" id="76">
            <value>the work</value>
          </definition>  
        </term>

A definition must have a UNIQUE numeric id (by sequence), or be referenced with the attribute ref="#id"
A definition can have at most one image element defined by a UNIQUE numeric id, or referenced by ref="#id",related to a file (by id or ref).
A definition can have at most one sound element defined by a UNIQUE numeric id, or referenced by ref="#id",related to a file (by id or ref).

##Translation
Translations are included into definitions, only one translation per language.

A translation must have a UNIQUE numeric id (by sequence), or be referenced with the attribute ref="#id".

A referenced translation includes an (somewhere defined) translation as an element of the current definition.
There must be at most one translation per language.

        <term txt="time" priority="3">
          <definition type="noun" id="81">
            <value>period era</value>
            <translation lang="de" id="675" sp="s">
              <gender id="m" value="der" lang="de">
              <gender ref="f" priority="1" />
              <value>Zeit</value>
              <sound ref="92" />
            </translation>
            <translation ref="123" />
            ...
          </definition>
        </term>

A translation can have at most one image element defined by a UNIQUE numeric id, or referenced by ref="#id",related to a file (by id or ref).
A translation can have at most one sound element defined by a UNIQUE numeric id, or referenced by ref="#id",related to a file (by id or ref).

###Gender and singular or plural (sp)

Genders are included into translations, the gender with priority 1 is used as the most common in case of ambiguity.

A gender is composed of the text that is added to the word, an id to indicate (m=masculine,f=feminine,n=neutral), a lang attribute (optional), and a value (the text value).
The lang attribute indicates the language of the gender. If no lang is supplied, the language of the translation is used as default.
A gender can be referenced by an ref="id" attribute linked to an existing gender.
The pair (id,lang) uniquely identifies the gender.

The attribute sp is used to give the singular or plural value: (s=singular,p=plural)

###Syllables, Chars, Pronunciation, Transliteration

The elements are included into translation element. If the element is of list type, its name is plural (with s) and contains multiple singular elements inside (without the s).

If the element is singular (like pronunciation and transliteration), it is written as is.

            <translation lang="zh-TW">
              <syllables count="2" ipa="xi-huan">
                <syllable ipa="xi">喜</syllable>
                <syllable ipa="huan">歡</syllable>
              </syllables>
              <chars>
                <char unicode="559C">
                  <value>喜</value>
                  <meaning>like, love, enjoy</meaning>
                  <pronunciation type="pinyin">Xǐ</pronunciation>
				  <image ref="34" />
		  		  <drawing ref="63" />
				  <radicals>
			        <char...>...</char>
					...
				  </radicals>
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

A syllable or syllables element can contain an ipa attribute, indicating the IPA sounds of the syllable(s), separated by '-'. 
(International Phonetic Alphabet : www.internationalphoneticalphabet.org)

A char must have a UNIQUE unicode (hex value). The unicode block name of the character can be given by the block attribute (optional).
A char can have at most one image element , representing the image of the character, related to a file (by id or ref).
A char can have at most one drawing element, representing the way to draw the character, related to a file (by id or ref).
A char can have at most one sound element, representing the sound of the character, related to a file (by id or ref).
A char can have a list of its component radical characters (radicals element).

##Conjugation,Mode,Time,Sentence,Persons
Conjugations are included into translation elements of definition of type "verb"

          <definition type="verb">
            <value>to be</value>
            <translation lang="en">
		          <conjugation id="51">
          			<mode name="Infinitive" id="67">
          				<time name="Present" id="45">
						<sentence id="73">
	          				<person name="I" />
							<value>am</value>
							<alt></alt>
						</sentence>
						<sentence id="74">
	          				<person name="You" />
							<value>are</value>
						</sentence>
						<sentence ref="74" />
          					...
          				</time>
          			</mode>
				<mode ref="34" />
          		</conjugation>
          </translation>
          </definition>
	
A conjugation,mode,time or sentence must have a UNIQUE numeric id (by sequence), or be referenced with the attribute ref="#id".
A person name must have a UNIQUE string id.

##Phrase(s)
A phrase is a definition of type="phrase" related to a null term (without txt)

        <term priority="3" id="132">
          <definition type="phrase">
          </definition>
        </term>

##Relations

Relations between objects (definitions) can be given by inclusions:
(The definition with id 43 is related to definition with id 62).

        <definition id="43" type="phrase">
 		  ...
		  <relation ref="62" type="antonym" />
        </definition>

#Files
Files represent binary content, like images, sounds, etc.
Each file is created with a UNIQUE numeric id (by sequence), or be referenced with the attribute ref="#id".
Binary content can be related to many objects like definitions, characters, etc. independent of the type.

#Ids

Term, definition and translation ids are independent but must have no duplicates.
The user must verify the correctness of the data and references.

##Sequences
While more ids are used, its values increase with a sequence, the user can change the sequence and use the new ids in the newly added objects and references.

        <dictionary>
			<sequences>
				<term value="123" />
				<definition value="23" />
				<translation value="84" />
				<file value="29" />
				<conjugation value="34" />
				<mode value="34" />
				<time value="34" />
				<sentence value="34" />
			</sequences>
        </dictionary>

The developer should provide a program to verify the data before committing changes to repository.

##Xml schema file and verification

A xml schema file should be included in the project to verify the correct structure and data types of the elements described.
        
        schema.xsd
        
#Database and Xml
Data is written in xml because it is text easy to read and change with compare editors, although at runtime it is more efficient to use sql (because of the Foreign Key constraints).
The xml file can be converted or imported easily to SQL. Database structures are located into the sql folder. 

The Xml file is
        
        cosmodict.xml
        
and all changes should be made to this file by the contributors.

Please verify that the previous changes did not delete all data before writing.
