package com.example.backend.parser;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
public class JavaCodeParser {

    public JavaFileAnalysis parseJavaFile(Path javaFile) throws IOException {

        CompilationUnit compilationUnit =
                StaticJavaParser.parse(javaFile);

        JavaFileAnalysis analysis = new JavaFileAnalysis();

        // Package
        compilationUnit.getPackageDeclaration()
                .ifPresent(packageDeclaration ->
                        analysis.setPackageName(
                                packageDeclaration.getNameAsString()
                        )
                );

        // Imports
        for (ImportDeclaration importDeclaration :
                compilationUnit.getImports()) {

            analysis.getImports().add(
                    importDeclaration.getNameAsString()
            );
        }

        // Classes and interfaces
        for (ClassOrInterfaceDeclaration type :
                compilationUnit.findAll(
                        ClassOrInterfaceDeclaration.class)) {

            ClassAnalysis classAnalysis = new ClassAnalysis();

            classAnalysis.setName(type.getNameAsString());
            classAnalysis.setInterface(type.isInterface());

            // Modifiers
            for (Modifier modifier : type.getModifiers()) {
                classAnalysis.getModifiers()
                        .add(modifier.getKeyword().asString());
            }

            // Extends
            type.getExtendedTypes().forEach(extendedType ->
                    classAnalysis.getExtendsClasses()
                            .add(extendedType.getNameAsString())
            );

            // Implements
            type.getImplementedTypes().forEach(implementedType ->
                    classAnalysis.getImplementedInterfaces()
                            .add(implementedType.getNameAsString())
            );

            // Fields
            type.getFields().forEach(field -> {

                for (VariableDeclarator variable :
                        field.getVariables()) {

                    FieldAnalysis fieldAnalysis =
                            new FieldAnalysis();

                    fieldAnalysis.setName(
                            variable.getNameAsString()
                    );

                    fieldAnalysis.setType(
                            variable.getType().asString()
                    );

                    for (Modifier modifier :
                            field.getModifiers()) {

                        fieldAnalysis.getModifiers()
                                .add(modifier.getKeyword().asString());
                    }

                    classAnalysis.getFields()
                            .add(fieldAnalysis);
                }
            });

            // Constructors
            for (ConstructorDeclaration constructor :
                    type.getConstructors()) {

                ConstructorAnalysis constructorAnalysis =
                        new ConstructorAnalysis();

                constructorAnalysis.setName(
                        constructor.getNameAsString()
                );

                constructorAnalysis.setParameters(
                        constructor.getParameters()
                                .stream()
                                .map(parameter ->
                                        parameter.getType().asString()
                                                + " "
                                                + parameter.getNameAsString()
                                )
                                .toList()
                );

                classAnalysis.getConstructors()
                        .add(constructorAnalysis);
            }

            // Methods
            for (MethodDeclaration method :
                    type.getMethods()) {

                MethodAnalysis methodAnalysis =
                        new MethodAnalysis();

                methodAnalysis.setName(
                        method.getNameAsString()
                );

                methodAnalysis.setReturnType(
                        method.getType().asString()
                );

                methodAnalysis.setParameters(
                        method.getParameters()
                                .stream()
                                .map(parameter ->
                                        parameter.getType().asString()
                                                + " "
                                                + parameter.getNameAsString()
                                )
                                .toList()
                );

                for (Modifier modifier :
                        method.getModifiers()) {

                    methodAnalysis.getModifiers()
                            .add(modifier.getKeyword().asString());
                }

                classAnalysis.getMethods()
                        .add(methodAnalysis);
            }

            analysis.getClasses().add(classAnalysis);
        }

        return analysis;
    }

    // =========================================================
    // Java File
    // =========================================================

    public static class JavaFileAnalysis {

        private String packageName;

        private List<String> imports =
                new ArrayList<>();

        private List<ClassAnalysis> classes =
                new ArrayList<>();

        public String getPackageName() {
            return packageName;
        }

        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }

        public List<String> getImports() {
            return imports;
        }

        public List<ClassAnalysis> getClasses() {
            return classes;
        }
    }

    // =========================================================
    // Class
    // =========================================================

    public static class ClassAnalysis {

        private String name;

        private boolean isInterface;

        private List<String> modifiers =
                new ArrayList<>();

        private List<String> extendsClasses =
                new ArrayList<>();

        private List<String> implementedInterfaces =
                new ArrayList<>();

        private List<FieldAnalysis> fields =
                new ArrayList<>();

        private List<ConstructorAnalysis> constructors =
                new ArrayList<>();

        private List<MethodAnalysis> methods =
                new ArrayList<>();

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isInterface() {
            return isInterface;
        }

        public void setInterface(boolean anInterface) {
            isInterface = anInterface;
        }

        public List<String> getModifiers() {
            return modifiers;
        }

        public List<String> getExtendsClasses() {
            return extendsClasses;
        }

        public List<String> getImplementedInterfaces() {
            return implementedInterfaces;
        }

        public List<FieldAnalysis> getFields() {
            return fields;
        }

        public List<ConstructorAnalysis> getConstructors() {
            return constructors;
        }

        public List<MethodAnalysis> getMethods() {
            return methods;
        }
    }

    // =========================================================
    // Field
    // =========================================================

    public static class FieldAnalysis {

        private String name;

        private String type;

        private List<String> modifiers =
                new ArrayList<>();

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<String> getModifiers() {
            return modifiers;
        }
    }

    // =========================================================
    // Constructor
    // =========================================================

    public static class ConstructorAnalysis {

        private String name;

        private List<String> parameters =
                new ArrayList<>();

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<String> getParameters() {
            return parameters;
        }

        public void setParameters(List<String> parameters) {
            this.parameters = parameters;
        }
    }

    // =========================================================
    // Method
    // =========================================================

    public static class MethodAnalysis {

        private String name;

        private String returnType;

        private List<String> parameters =
                new ArrayList<>();

        private List<String> modifiers =
                new ArrayList<>();

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getReturnType() {
            return returnType;
        }

        public void setReturnType(String returnType) {
            this.returnType = returnType;
        }

        public List<String> getParameters() {
            return parameters;
        }

        public void setParameters(List<String> parameters) {
            this.parameters = parameters;
        }

        public List<String> getModifiers() {
            return modifiers;
        }
    }
}