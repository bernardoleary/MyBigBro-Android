package com.infostructure.mybigbro.services;

import junit.framework.Assert;
import junit.framework.TestCase;

public class DataAccessServiceTest extends TestCase {

    public void testGetDeviceName() throws Exception {
        // Arrange
        DataAccessService dataAccessService = DataAccessService.getInstance();
        // Act
        String randomWord = dataAccessService.getDeviceName();
        // Assert
        Assert.assertNotNull("Random word is NULL", randomWord);
    }
}