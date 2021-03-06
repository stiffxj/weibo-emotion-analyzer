package edu.pku.emotion.feat;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import edu.pku.instance.Sentence;
import edu.pku.instance.Weibo;
import edu.stanford.nlp.trees.TypedDependency;

/**
 * 
 * @author intfloat@pku.edu.cn
 * @author labyrinth@pku.edu.cn
 *
 */
public class SyntacticFeatureExtractor implements FeatureExtractorInterface {
    private static final String ParseWord_="ParseWord_";
    private String[] Relation={"amod","advmod","assmod"};

    @Override
    public void extract(Weibo weibo, List<Feature> features) {
        // TODO Auto-generated method stub
        Collection<TypedDependency> parserResult = weibo.getParseResult();
        this.addSyntaticFeature(parserResult, features);
        return;
    }    
    
    @Override
    public void extract(Sentence sentence, List<Feature> features) {
        // TODO Auto-generated method stub
        Collection<TypedDependency> parserResult=sentence.getParseResult();
        this.addSyntaticFeature(parserResult, features);
        return;
    }
  
    /**
     * 
     * @param parserResult
     * @param features
     */
    private void addSyntaticFeature(Collection<TypedDependency> parserResult, List<Feature> features) {                
        HashMap<String, Integer> counter = new HashMap<String, Integer>();
        for(TypedDependency w : parserResult) {
            String s = w.toString();
            for(int i=0;i<Relation.length;i++) {
                if(s.contains(Relation[i])) {
                    String[] temp = s.split(",");
                    if(temp.length != 2) continue;
                    temp[0] = getWord(temp[0]);
                    temp[1] = getWord(temp[1]);
                    if(temp[0] == null || temp[1] == null) continue;                      
                    for (String word : temp) {
                        if (!counter.containsKey(word)) counter.put(word, 1);
                        else counter.put(word, counter.get(word) + 1);
                    }
                }
            }
        }
       
        for (String key : counter.keySet()) {
            features.add(new Feature(ParseWord_ + key, counter.get(key)));
        }
        return;
    }
    

    private String getWord(String aline)
    {
        if(aline.contains("("))
        {
            int index1=aline.indexOf("(");
            int index2=aline.indexOf("-");
            String temp=aline.substring(index1+1, index2);
            return temp;
        }
        else
        {
            int index1=aline.indexOf("-");
            String temp=aline.substring(0,index1);
            temp=temp.replace(" ", "");
            return temp;
        }
    }

}
