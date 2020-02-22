//package com.project.najdiprevoz;
//
//import org.hibernate.QueryException;
//import org.hibernate.dialect.PostgreSQL94Dialect;
//import org.hibernate.dialect.function.SQLFunction;
//import org.hibernate.engine.spi.Mapping;
//import org.hibernate.engine.spi.SessionFactoryImplementor;
//import org.hibernate.type.BooleanType;
//import org.hibernate.type.Type;
//
//import java.sql.Types;
//import java.util.List;
//
//public class PingaPostgreSQLDialect extends PostgreSQL94Dialect {
//    public static final String STRING_ARRAY_INTERSECTION = "string_array_intersection";
//    public static final String LONG_ARRAY_INTERSECTION = "long_array_intersection";
//    public static final String FULL_TEXT_SEARCH = "full_text_search";
//
//    public PingaPostgreSQLDialect() {
//        this.registerFunction(STRING_ARRAY_INTERSECTION, new PgStringArrayIntersectionFunction());
//        this.registerFunction(LONG_ARRAY_INTERSECTION, new PgLongArrayIntersectionFunction());
//        this.registerFunction(FULL_TEXT_SEARCH, new PgFullTextSearchFunction());
//        this.registerColumnType(Types.JAVA_OBJECT, "jsonb");
//    }
//}
//
//
//class PgLongArrayIntersectionFunction extends PgBasePredicateFunction {
//    @Override
//    public String render(Type firstArgumentType, List arguments, SessionFactoryImplementor factory)
//            throws QueryException {
//        if (arguments == null || arguments.size() != 2) {
//            throw new RuntimeException("Wrong usage of the array intersection function !!");
//        }
//        String fieldName = (String) arguments.get(0);
//        String joinedCollection = (String) arguments.get(1);
//        return String.format("%s && %s::bigint[]", fieldName, joinedCollection);
//    }
//
//}
//
//class PgStringArrayIntersectionFunction extends PgBasePredicateFunction {
//    @Override
//    public String render(Type firstArgumentType, List arguments, SessionFactoryImplementor factory)
//            throws QueryException {
//        if (arguments == null || arguments.size() != 2) {
//            throw new RuntimeException("Wrong usage of the array intersection function !!");
//        }
//        String fieldName = (String) arguments.get(0);
//        String joinedCollection = (String) arguments.get(1);
//        return String.format("%s && %s::text[]", fieldName, joinedCollection);
//    }
//
//}
//
//
//class PgFullTextSearchFunction extends PgBasePredicateFunction {
//
//    @Override
//    public String render(Type firstArgumentType, List arguments, SessionFactoryImplementor factory)
//            throws QueryException {
//        if (arguments == null || arguments.size() != 2) {
//            throw new RuntimeException("Wrong usage of the pg full text search function !!");
//        }
//        String fieldName = (String) arguments.get(0);
//        String keywords = (String) arguments.get(1);
//        return String.format("%s @@ to_tsquery(%s)", fieldName, keywords);
//    }
//}
//
//abstract class PgBasePredicateFunction implements SQLFunction {
//    @Override
//    public boolean hasArguments() {
//        return true;
//    }
//
//    @Override
//    public boolean hasParenthesesIfNoArguments() {
//        return false;
//    }
//
//    @Override
//    public Type getReturnType(Type firstArgumentType, Mapping mapping) throws QueryException {
//        return new BooleanType();
//    }
//}
//
