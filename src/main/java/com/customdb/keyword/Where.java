package com.customdb.keyword;

import com.customdb.util.ReflectionUtil;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.function.Predicate;

import static com.customdb.keyword.EnumComparisonOperator.*;
import static com.customdb.keyword.EnumLogicalOperator.*;

/**
 * @author mingbozhang
 */
@Builder
public class Where {

    @Getter
    private String criteriaString;

    public Where(String criteriaString) {
        this.criteriaString = criteriaString;
    }

    public Predicate buildPredicateByDataType(Class dataType) {

        if (StringUtils.isBlank(criteriaString)) {
            return o -> true;
        }

        //"name=1 and age=2 or age=1"
        //and 优先级高于or
        Predicate orStart = o -> false;
        String[] orElementArr = criteriaString.split(OR.getSymbol());

        for (String andStr : orElementArr) {

            String[] comparisonStrArr = andStr.split(AND.getSymbol());
            Predicate andStart = o -> true;

            for (String comparisionStr : comparisonStrArr) {
                Predicate comparisionPredicate = createComparisonPredicate(comparisionStr, dataType);
                andStart = andStart.and(comparisionPredicate);
            }
            orStart = orStart.or(andStart);
        }

        return orStart;
    }

    private Predicate createComparisonPredicate(String comparisonStr, Class dataType) {

        EnumComparisonOperator comparisonOperator;

        if (comparisonStr.indexOf(LE.getSymbol()) >= 0) {
            comparisonOperator = LE;
        } else if (comparisonStr.indexOf(GE.getSymbol()) >= 0) {
            comparisonOperator = GE;
        } else if (comparisonStr.indexOf(LT.getSymbol()) >= 0) {
            comparisonOperator = LT;
        } else if (comparisonStr.indexOf(GT.getSymbol()) >= 0) {
            comparisonOperator = GT;
        } else if (comparisonStr.indexOf(EQ.getSymbol()) >= 0) {
            comparisonOperator = EQ;
        } else {
            throw new IllegalArgumentException("Where语句不合法");
        }

        //例如 age<20
        String[] elementArr = comparisonStr.split(comparisonOperator.getSymbol());
        String fieldName = elementArr[0].trim();
        Field field = ReflectionUtil.getDeclaredField(dataType, fieldName);
        Class fieldType = field.getType();

        Comparable comparableValue = (Comparable) ReflectionUtil.parseValueByType(fieldType, elementArr[1].trim());

        return o -> {
            try {
                return comparisonOperator.compute((Comparable) field.get(o), comparableValue);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException(e);
            }
        };
    }
}
