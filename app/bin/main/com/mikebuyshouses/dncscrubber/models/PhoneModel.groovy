package com.mikebuyshouses.dncscrubber.models

import com.mikebuyshouses.dncscrubber.constants.Constants
import com.mikebuyshouses.dncscrubber.datamanip.YesNoBooleanConverter
import com.mikebuyshouses.dncscrubber.enums.PhoneTypes
import com.mikebuyshouses.dncscrubber.utils.DateUtils
import com.mikebuyshouses.dncscrubber.utils.NumberUtils
import com.mikebuyshouses.dncscrubber.utils.StringUtils
import org.apache.commons.collections4.MultiValuedMap

import java.time.ZonedDateTime

public class PhoneModel {
	boolean isDNC;

	int score;

	PhoneTypes phoneType;

	String phoneNumber;

	ZonedDateTime date;

	public PhoneModel() {
		super();
	}

	public PhoneModel(boolean isDNC, int score, PhoneTypes phoneType, String phoneNumber, ZonedDateTime date) {
		super();
		this.isDNC = isDNC;
		this.score = score;
		this.phoneType = phoneType;
		this.phoneNumber = phoneNumber;
		this.date = date;
	}

	public boolean isDNC() {
        return isDNC;
    }

	public void setDNC(boolean dNC) {
		isDNC = dNC;
	}

	public int getScore() {
        return score;
    }

	public void setScore(int score) {
        this.score = score;
	}

    public PhoneTypes getPhoneType() {
        return phoneType;
	}

	public void setPhoneType(PhoneTypes phoneType) {
		this.phoneType = phoneType;
	}

	public String getPhoneNumber() {
        return phoneNumber;
    }

	public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
	}	

	public ZonedDateTime getDate() {
        return date;
    }

	public void setDate(ZonedDateTime date) {
        this.date = date;
	}

	@Override
    public String toString() {
		return "PhoneModel [isDNC=${isDNC}, score=${score}, phoneType=${phoneType}, phoneNumber=${phoneNumber}, date=${date}]";
    }

	public static List<PhoneModel> ExtractFromRawPhoneData(MultiValuedMap<String,Object> rawPhoneData) {
		List<PhoneModel> phoneModels = [];

		PhoneModel model = new PhoneModel();

		List<String> sortedMapKeys = rawPhoneData.keySet()
			// SOURCE: Phind AI
			.sort { String a, String b ->
				int aNum = NumberUtils.ExtractNumber(a);
				int bNum = NumberUtils.ExtractNumber(b);
				return aNum <=> bNum ?: a <=> b
			}
		sortedMapKeys.eachWithIndex({ String key, int idx ->
			model = this.BuildPhoneModel(model, rawPhoneData.get(key)[0], key);

			if ((idx < sortedMapKeys.size() - 1) && 
				(NumberUtils.ExtractNumber(sortedMapKeys[idx + 1]) == NumberUtils.ExtractNumber(sortedMapKeys[idx])))
				return;

			if (!StringUtils.IsNullOrEmpty(model.phoneNumber))
				phoneModels.add(model);

			model = new PhoneModel();
		})

		return phoneModels;
	}

	private static PhoneModel BuildPhoneModel(PhoneModel originalModel, Object rawValue, String key) {
		if (key.contains(Constants.DncKeyPart)) {
			originalModel.isDNC = new YesNoBooleanConverter().convertStringToBoolean(rawValue);

			return originalModel;
		}

		if (key.contains(Constants.ScoreKeyPart)) {
			originalModel.score = NumberUtils.ParseInt(rawValue);

			return originalModel;
		}

		if (key.contains(Constants.TypeKeyPart)) { 
			if (!StringUtils.IsNullOrEmpty(rawValue))
				originalModel.phoneType = PhoneTypes.FromTextValue(rawValue);

			return originalModel;
		}

		if (key.contains(Constants.NumberKeyPart)) {
			originalModel.phoneNumber = rawValue;

			return originalModel;
		}

		if (key.contains(Constants.DateKeyPart)) {
			if (!StringUtils.IsNullOrEmpty(rawValue)) 
				originalModel.date = DateUtils.ParseDateTime(rawValue);

			return originalModel;
		}

		throw new IllegalArgumentException("Unrecognized key '${key}'");
	}
}