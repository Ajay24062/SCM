package com.scm.helpers;

public class ResourceNotFoundException extends RuntimeException {
public ResourceNotFoundException(String message){

}
public ResourceNotFoundException()
{
    super("ResourceNotFoundException");
}
}
