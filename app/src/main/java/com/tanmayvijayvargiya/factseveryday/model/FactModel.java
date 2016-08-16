package com.tanmayvijayvargiya.factseveryday.model;

import com.raizlabs.android.dbflow.sql.language.Select;
import com.tanmayvijayvargiya.factseveryday.di.component.AppComponent;
import com.tanmayvijayvargiya.factseveryday.utils.ValidateUtil;
import com.tanmayvijayvargiya.factseveryday.vo.Fact;
import com.tanmayvijayvargiya.factseveryday.vo.Fact_Table;

import java.util.List;

/**
 * Created by tanmayvijayvargiya on 16/08/16.
 */
public class FactModel {

    private AppComponent mAppComponent;

    public FactModel(AppComponent mAppComponent) {
        this.mAppComponent = mAppComponent;
        mAppComponent.inject(this);
    }

    public void save(Fact fact){
        fact.validate();
        if(!fact.exists())
            fact.save();
        else
            fact.update();

    }



    public void saveAll(List<Fact> facts){
        facts =(List<Fact>)  ValidateUtil.pruneInvalids(facts);

        for(Fact fact : facts){
            if(!fact.exists())
                fact.save();
            else
                fact.update();
        }
    }

    public List<Fact> loadAll(){
        return new Select().from(Fact.class).queryList();
    }

    public Fact loadById(String factId) {
        return new Select().from(Fact.class).where(Fact_Table._id.is(factId)).querySingle();
    }
}
