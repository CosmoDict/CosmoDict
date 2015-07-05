# CosmoDict
Dictionary of Frequent Terms Translated to All Languages Flat

The dictionary is a multi-contributions project created to provide a list of common terms translated to many languages, like this (table):

	term  type  definition          english german  french  italian spanish russian chinese(Traditional) ...(etc)

	be    verb  to exist or live    be      sein    être    essere  ser     быть    是

	man  noun  person              man     Mann    homme   uomo    hombre  ...

(etc)

The data is useful to learn many languages at once, translate terms from language to language, and statistics.

The contributors are intended to be proficient language speakers and edit the data adding terms, translations, phrases and (language-free) images, sound files with pronunciation, etc.

Speakers from all the world are welcome to contribute and fill the data with ease.

The initial data goals are near 1000 verbs, 2000 nouns, 1000 adjs and 1000 other including numerals, prepositions, pronouns, etc.


#How to contribute

The dictionary is written in xml, UTF-8 charset, just edit the dictionary, verify and update the repository (git). 

##Dictionary

The root element is "dictionary".
        
        <?xml version="1.0" encoding="UTF-8"?>
        <dictionary>...</dictionary>

Other elements are:

##Term(s)

term elements are included into the terms element of dictionary.

A term is a text written in english (case insensitive written with chars [a-z][0-9]'-_ and space only) to index the definitions. 
Spaces are allowed but not two consecutive, and not at the beginning or the end of the string.
The length of the text must not exceed the 64 characters.

        <dictionary>
          <terms>
            <term id="3" txt="be" priority="3">...</term>
          </terms>
        </dictionary>

A term must have a UNIQUE numeric id (by sequence), and a UNIQUE txt value (can be null).

A term can have a group attribute to classify the terms into groups.

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
            <type id="synonym" rel="true" />
            <type id="antonym" rel="true" />
            <type id="null"/>
          </types>
        </dictionary>

A type can have a rel="true" attribute to indicate that this type is used in relation pairs (between definitions).

##Language(s)

Include the supported languages identified by its ISO 693 code (ISO 639-3). 

The content of each lang element is the language name written in the original language.
The attributes of lang element are the fields of the ISO 639 standard. The id is the 3-letter id iso code.

        <dictionary>
          <languages>
            <lang id="eng" name="English" p1="en" p2b="eng" p2t="eng" type="L" scope="I" ></lang>
            <lang id="rus" name="Russian" p1="ru" p2b="rus" p2t="rus" type="L" scope="I" >Русский язык</lang>
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

A definition can have at most one image element defined by a UNIQUE numeric id, or referenced by ref="#id",related to a file.

A definition can have at most one sound element defined by a UNIQUE numeric id, or referenced by ref="#id",related to a file.

The image and the sound files of a definition are expected to be language-independent.

##Translation
Translations are included into definitions, only one translation per language.

A translation must have a UNIQUE numeric id (by sequence), or be referenced with the attribute ref="#id".

A referenced translation includes an (somewhere defined) translation as an element of the current definition.

There must be at most one translation per language.

        <term txt="time" priority="3">
          <definition type="noun" id="81">
            <value>period era</value>
            <translation lang="deu" id="675" sp="s">
              <gender id="m" value="der" lang="deu">
              <gender ref="f" priority="1" />
              <value>Zeit</value>
              <sound ref="92" />
            </translation>
            <translation ref="123" />
            ...
          </definition>
        </term>

A translation can have at most one image element defined by a UNIQUE numeric id, or referenced by ref="#id",related to a file.

A translation can have at most one sound element defined by a UNIQUE numeric id, or referenced by ref="#id",related to a file.

A translation can have at most one infinitive element with attribute type=("r" regular,"i" irregular), and the text value. 

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

            <translation lang="cmn">
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

A char can have at most one image element , representing the image of the character, related to a file.

A char can have at most one drawing element, representing the way to draw the character, related to a file.

A char can have at most one sound element, representing the sound of the character, related to a file.

A char can have a list of its component radical characters (radicals element).

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

A relation can have a (numerical int) ord attribute to indicate the ordinal number of the relation, relations are sorted from lowest ord to hightest ord.

A relation can have a (base64 bigint) weight attribute to indicate the weight of the relation, weights give priorities to the relations from highest to lowest.

A relation must have a type attribute to indicate the type. The corresponding type must have a rel="true" attribute.

#Files
Files represent binary content, like images, sounds, etc.

Each file is created with a UNIQUE numeric id (by sequence), or be referenced with the attribute ref="#id".

Xml elements representing binary content can have a mime attribute to indicate their mime type.

Binary content can be related to many objects like definitions, characters, etc. independent of the type.

##Grammar

There are two types of grammar elements
	
	(1) The definition of grammar.
	(2) The grammar references.

###Grammar Objects
The xml type of a grammar object is the same for all the objects. Its element have a name attribute, a type attribute and a use attribute.
The pair (type,name) is unique. 

To define a grammar object, its element must have the id attribute. 
To reference the object, it must have the ref attribute.
Object definitions can't have both attributes.

The grammar references often includes only the ref="#id" attribute, to indicate the use of the already-defined object, but can also define and use the object in other ways.

The use attribute is to indicate the use type of the object: (i="include" , o="override", d="delete").

If the element has only one attribute id or ref, the use attribute is ignored (default=i). If both attributes are present, the value can't be i(include) and the default is o(override).

The sequence (id generator) for all the grammar objects is unique.

###Grammar Objects Definitions

The grammar definitions are written as elements of dictionary:

        <dictionary>
          <grammar>...</grammar>
        </dictionary>

###Grammar Object References

The grammar objects are used by writting it as elements of translation:

        <translation>
          <grammar>...</grammar>
        </translation> 

##Grammar Types

The grammar types are free to take any string value.
These are defined into the dictionary grammar types element:

        <dictionary>
          <grammar>
	          <types>
				<type id="m" name="mode" />
				<type id="t" name="time" />
				<type id="s" name="sentence" />
				<type id="p" name="pronom" />
				<type id="r" name="property" />
				<type id="v" name="var" />
				<type id="l" name="value" />
				<type id="f" name="function" />
				<type id="e" name="eval" />
				<type id="x" name="text" />
				<type id="0" name="verb" />
				<type id="1" name="noun" />
				<type id="2" name="adj" />
				<type id="3" name="meta" />
				<type id="4" name="infinitive" />
	          </types>
          </grammar>
        </dictionary>

##Rule element.
The grammar objects can be written in xml using the rule element. rule elements can contain other rule elements.

	<grammar>
		<rule type="m" name="Infinitive">
			<rule type="t" name="Present">
				...
			<rule>
		<rule>
	</grammar>

###Equivalents of rule elements.
 
	<mode name="Infinitive">																							<rule type="m" name="Infinitive">
	<time name="Present">																								<rule type="t" name="Present">
	<pronom name="je" />										<value type="pronom" value="je" />						<rule type="p" var="pronom" value="je" />
	<(sentence|var|value|function) name="result">				<value type="eval" name="result">...</value>			<rule type="(s|v|l|f)" name="result">
	<(verb|noun|adj|infinitive) />								<value type="(verb|noun|adj|inf)"/>						<rule type="(0|1|2|4)" var="(verb|noun|adj|inf)" />
	<property name="lang" value="eng" />						<value type="prop" name="lang" value="eng" />			<rule type="r" name="lang" value="eng" />
	<text>?</text>												<value type="text">?</value>							<rule type="x" >?</var>
	<alt>...</alt>												<value type="text" name="#[0-9]+">...</value>			<rule type="3" name="#[0-9]+" >?</var> (name is automatically assigned).
	<eval f="if-then-else"></eval>								<value type="eval" f="if-then-else">...</value>			<rule type="e" f="if-then-else" >?</var>

###Properties of rule elements.
A rule element can have a property element to indicate a property of the rule.
These are equivalent:

		<rule lang="ita">						<rule>																<rule>
													<property name="lang" value="ita" />					 			<property name="lang">ita</property>
		</rule>									</rule>																</rule>

###Grammar References
When the grammar objects are being used inside a translation element, they must be referenced with the ref="#id" attribute.

Trying to define it, generates an error except when both attributes id and ref are present. Also trying to define a person gives an error.

Instead of using the already defined sentences of the parent element to generate text, the newly defined sentences are used, depending on the value of the use attribute.

This example indicates to include the time referenced by the id="45" with all its contents (but not his parent mode), overriding the sentence with id="43" with the new sentence with id="73", and including the (possibly not included before) sentence with id="74".

			<grammar>
				<time ref="45">
					<sentence id="73" ref="43">
	          			<pronom name="I" />
	          			<text>invent</text>
					</sentence>
					<sentence ref="74" />
				</time>
			</grammar>

###Person(s)
Although not a grammar object, the person elements are defined into the dictionary grammar persons element (multiplicity: many).

Attributes of person element are: id UNIQUE number (generated by sequence), sp (s=singular,p=plural,n=neutral), ord="[0-9]+" ordinal, lang="#language id".

        <grammar>
        	<persons>
	          <person>...</person>
        	</persons>
        </grammar>

####Pronom

The pronom element is a special type used to group many ways to refer to person(s), many to many.

A person element can be related to many pronom elements and a pronom with the same name can be related to many persons. 

	<person id="47" ord="1" sp="s" lang="eng">
		<pronom name="j'" />
		<pronom name="all" />
	</person>

The pronom element is used also in sentences.

When using the pronom, the person="#id" can be given to uniquely resolve the pair (person,pronom). 

###Examples of Grammar

	<dictionary>
		<grammar>
        	<persons>
				<person id="1" name="I" sp="s" ord="1" lang="eng">
					<pronom name="I"/>
					<pronom name="I'"/>
					<pronom name="all"/>
				</person>
				<person id="2" name="You" sp="s" ord="2" lang="eng">
					<pronom name="You"/>
					<pronom name="all"/>
				</person>
        	</persons>
			...
            <mode id="67" name="Infinitive">
               <time name="Present" id="45">
	               <sentence id="73">
	                    <pronom name="I" />
	                    <infinitive />
	               </sentence>
	               <sentence id="74">
	                    <pronom name="You" person="2" />
	                    <infinitive />
	               </sentence>
	                     ...
	           </time>
	        </mode>
		</grammar>
		<term id="23" txt="be">	
	      <definition type="verb">
	        <value>to be</value>
	        <translation lang="eng">
	              <grammar>
	                 <time name="Present" id="45">
		                 <sentence id="135" ref="73">
		                     <pronom name="I" />
		                     <value>am</value>
		                     <alt></alt>
		                 </sentence>
		                 <sentence id="136" ref="74" use="d" />
		                 ...
	                 </time>
	            	<mode ref="34" />
	            </grammar>
	      	</translation>
	      </definition>
	   </term>
	</dictionary>

##Values: function evaluation and variables.

A value is an element wich evaluates an expression to a text. All rule elements can produce text by evaluating them.

A value is used as a text generator with positional variables obtained from the context.

The return value from the current element is the result of processing each of its elements in order, concatenating the results. 
null results are coerced to empty strings.

The value can declare a variable with the result of its processing. The variable name is specified with the name attribute. In this case the element returns the empty string.

A value can be the result of evaluating a (previously-defined) function to a list of arguments. 

The function name can be given by the f="name" attribute, or the ref="#id" attribute.

The arguments of the function is the collection of results obtained from its elements that are not variables (elements without the name="" attribute).

There are some built-in default functions, mainly string functions: (substring,contains...), operands (if, if-then-else, equal, not-equal,...). 

The var="name" refers to obtain the value of the variable with that name and return the result.

To represent static text values, the elements must provide the value in the value attribute or the node text value (inner text), these are taken in order of precedence. 

	<sentence id="23">
		<pronom name="je" />
		<text> </text>
		<infinitive />						====>			je parle
		<text>e</text>
	</sentence>

If the evaluation of an element generates an error, the result is the "%Error%" string.

###Built-in functions
Functions are independent from platform and programming language.

If an argument is not given, the function takes the null value as default.

		Function		Abbreviation	#Args			Equivalent expression														Observations
		
		empty			em				1				(arg0 == null || arg0 == "")? "": "1" 		

		num				n				1				empty(arg0)	? "0":numeric value of arg0 (int or float)						internal use.
		bool			b				1				empty(arg0) ? "":"1"
		not				!				1				(!bool(arg0))	? "":"1"
		equals			eq				2				arg0 equals arg1	? "":"1"			
		not-equal		ne				2				!(arg0 equals arg1)	? "":"1"	
		
		great-than		gt				2				num(arg0) > num(arg1)	? "":"1"						
		less-than		lt				2				num(arg0) < num(arg1)	? "":"1"						
		gt-or-eq		ge				2				num(arg0) >= num(arg1)	? "":"1"					
		lt-or-eq		le				2				num(arg0) <= num(arg1)	? "":"1"

		lex-gt			xg				2				arg0 > arg1	? "":"1"														lexicographical ordering					
		lex-lt			xl				2				arg0 < arg1	? "":"1"						
		lex-ge			xge				2				arg0 >= arg1	? "":"1"					
		lex-le			xle				2				arg0 <= arg1	? "":"1"
							
		if-then-else	ie				3				empty(arg0)? arg1:arg2
		
		starts-with		sw				2				arg0 starts with arg1	? "":"1"			
		ends-with		ew				2				arg0 ends with arg1		? "":"1"				
		length			ln				1				length of arg0
		trim			tr				1				delete beginning or ending spaces, newlines, tabs and \r\f chars in arg0.				
		contains		ct				2				arg0 contains arg1	? "":"1"
		index-of		ix				2				index of first occurrence of arg1 in arg0	("" or result >= 0 as string)
		substring		sb				3				substring of arg0 				
															from index arg1 
															to index arg2
		concat			+				2				arg0 concatenated with arg1
		pad				pd				3				pad arg0 with string(char) arg1 up to length <= arg2.
		replace			rp				3				replaces in string arg0 occurrences of arg1 with arg2.
		upper			uc				2				arg0 to upercase
		lower			lc				2				arg0 to lowercase
		cap				cp				1				change first char in each word of arg0 to uppercase to lowercase			(words are separated by (space|tab|\n|\r|\f)+)
		char-at			ch				2				return the character at index arg1 of arg0, if any, otherwise return "".
		
		md5				md5				1				return the md5 hash of arg0
		b64-encode		64e				1			    return the base64 encoded value of arg0
		b64-decode		64d				1			    return the base64 decoded value of arg0
		hex				hex				1				hex value of arg0 (0xff)
		
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
				<rule value="34" />
				<person value="6" />
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
