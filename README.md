# DictionaryRead

//Download \
http://www.lingoes.cn/download/dict/ld2/English-Vietnamese%20Dictionary.ld2 \
MongoDB 4.2.3 \ 

//Paste the downloaded file path to LingoesLd2Reader, line 90 \
Run the LingoesLd2Reader main, get the OUTPUT file \

//Structure of OUTPUT file: \
Word(key) = @Word(value) / Pronunciation / - Meaning = Example in English + Example in Vietnamese \
-----------|--------------------------------------This can happen multiple times-----------------| \

//Paste the OUTPUT file path to RegexToHash, line 94  \
RegexToHash:\
store the dictionary in 2 HashMapValuedHashSet class (to remove redundant and duplicate): WordKeyWordValue and WordValueMeaning\


//Paste the OUTPUT file path to MongoDBinsert, line 25 \
Run the MongoDBinsert main \
Then run the MongoDBquery main\
BETTER REGEX to capture pronunciation and handle meaning properly\
COLLECTION (MongoDB) provided text index with better query perfomance\
