from rake_nltk import Metric, Rake
import sys

def getKeyWords(s):
    keywords = ""
    r = Rake(language="english")
    r.extract_keywords_from_text(s)
    list = r.get_ranked_phrases()
    for word in list:
        keywords += word + ","
    keywords = keywords[:-1]
    return keywords

if __name__ == '__main__':
    a = ""
    for i in range(1, len(sys.argv)):
        a += str(sys.argv[i]) + " "
    a = a[:-1]
    print(getKeyWords(a))