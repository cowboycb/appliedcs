/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private Set<String> wordSet;
    private Map<String, List> lettersToWord;
    private Map<Integer, List> sizeToWords;
    private String[] alphabet = {
            "a", "b", "c", "d", "e", "f", "g", "h", "i", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "v", "x", "y", "z"
    };
    private int wordLength;

    public AnagramDictionary(Reader reader, Context context) throws IOException {
        wordSet = new HashSet<>();
        lettersToWord = new HashMap<>();
        sizeToWords = new HashMap<>();
        wordLength = DEFAULT_WORD_LENGTH;
        BufferedReader in = new BufferedReader(reader);
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordSet.add(word);
            String sorted = sortLetters(word);
            if (!lettersToWord.containsKey(sorted)){
                lettersToWord.put(sorted, new ArrayList());
            }
            lettersToWord.get(sorted).add(word);

            if (!sizeToWords.containsKey(word.length())){
                sizeToWords.put(Integer.valueOf(word.length()), new ArrayList());
            }
            sizeToWords.get(Integer.valueOf(word.length())).add(word);
        }
//        OutputStream os = new FileOutputStream(new File(context.getFilesDir(), "worddds.txt"));
//        Set<String> keys = lettersToWord.keySet();
//        for (String key : keys) {
//
//            os.write(key.getBytes());
//            os.write("\r\n".getBytes());
//            os.write(lettersToWord.get(key).toString().getBytes());
//            os.write("\r\n".getBytes());
//            os.flush();
//        }
//
//        os.close();
    }

    public boolean isGoodWord(String word, String base) {
        return word != null && word.trim().length()>0 && wordSet.contains(word) && !word.contains(base);
    }

    public List<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();
//        String sorted = sortLetters(targetWord);
        // is it a word
        Set<String> keys = lettersToWord.keySet();
        for (String key : keys) {
            for (String letter : alphabet) {
                if (sortLetters(targetWord+letter).equals(key)){
                    result.addAll(lettersToWord.get(key));
                }
            }
        }
        return result;
    }

    private String sortLetters(String word){
        char[] letters = word.toCharArray();
        Arrays.sort(letters);
        return new String(letters);
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
//        String sorted = sortLetters(targetWord);
        // is it a word
        Set<String> keys = lettersToWord.keySet();
        for (String key : keys) {
            for (String letter : alphabet) {
                if (sortLetters(word+letter).equals(key)){
                    result.addAll(lettersToWord.get(key));
                }
            }
        }
        return result;
    }

    public String pickGoodStarterWord() {
        String kelime;
        int numberOfAnagrams = 0;
        int trialCount = 0;
        do {
            trialCount ++;
            int rasgele = random.nextInt(wordSet.size());
            kelime = wordSet.toArray()[rasgele].toString();
            if (kelime.length() <= wordLength) {
                numberOfAnagrams = getAnagrams(kelime).size();
                Log.d("ANAGRAM", trialCount + " Bulunan Kelime " + kelime + ", Anagram Sayısı " + numberOfAnagrams);

                if (trialCount > 1000) {
                    Log.d("ANAGRAM", "100. denemeden dolayı çıkılıyor...");
                }
            }
        }while (numberOfAnagrams <MIN_NUM_ANAGRAMS);
        return kelime;
    }
}
