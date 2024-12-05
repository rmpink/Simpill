/* (C) 2023 */
package com.spacesloth.meditrack;

public class ArrayHelper {

    public int findMedicationById(Medication[] medicationArray, long id) {
        int pillIndex = -1;
        for (int index = 0; index < medicationArray.length; index++) {
            if (medicationArray[index].getId() == id) pillIndex = index;
        }
        return pillIndex;
    }

    public Medication[] deleteMedicationFromArray(Medication[] medicationArray, Medication medication) {
        int index = findMedicationById(medicationArray, medication.getId());
        Medication[] medicationArrayCopy = new Medication[medicationArray.length - 1];
        System.arraycopy(medicationArray, 0, medicationArrayCopy, 0, index);
        System.arraycopy(medicationArray, index + 1, medicationArrayCopy, index, medicationArray.length - index - 1);
        return medicationArrayCopy;
    }

    public Medication[] addMedicationToArray(Medication[] medicationArray, Medication medication) {
        Medication[] medicationArrayCopy = new Medication[medicationArray.length + 1];
        System.arraycopy(medicationArray, 0, medicationArrayCopy, 0, medicationArray.length);
        medicationArrayCopy[medicationArrayCopy.length - 1] = medication;
        return medicationArrayCopy;
    }
}
