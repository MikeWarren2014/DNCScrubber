package com.mikebuyshouses.dncscrubber

import org.apache.commons.collections4.MultiValuedMap
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap

import com.mikebuyshouses.dncscrubber.models.PhoneModel

import spock.lang.Specification

class PhoneModelTest extends Specification { 
	def "ExtractFromRawPhoneData should...extract from raw phone data"() {
		setup:
		def literal = [
			('Phone2_Score'):[95],
			('Phone3_Score'):[90],
			('Phone1_Score'):[100, ],
			('Phone1_Type'):["Land Line"],
			('Phone2_DNC'):["NO"],
			('Phone3_Type'):["Land Line"],
			('Phone2_Type'):["Land Line"],
			('Phone3_Last_Seen'):[],
			('Phone1_DNC'):["NO"],
			('Phone10_Last_Seen'):[],
			('Phone10_Number'):[3172830075],
			('Phone10_Score'):[93],
			('Phone10_Type'):["Land Line"],
			('Phone10_DNC'):["NO"],
			('Phone1_Last_Seen'):[],
			('Phone1_Number'):[3172830074],
			('Phone2_Number'):[3179267430],
			('Phone3_DNC'):["NO"],
			('Phone2_Last_Seen'):[],
			('Phone3_Number'):[3179265508]
		]

		MultiValuedMap<String, Object> map = this.createMultiValuedMapFrom(literal);

		when:
		List<PhoneModel> phoneModelList = PhoneModel.ExtractFromRawPhoneData(map)

		then:
		phoneModelList.size() == 4

	}

	def "ExtractFromRawPhoneData should be sorting the keys of the raw phone data"() {
		setup:
		def literal = [
				('Phone2_Score'):[95],
				('Phone3_Score'):[90],
				('Phone1_Score'):[100, ],
				('Phone1_Type'):["Land Line"],
				('Phone2_DNC'):["NO"],
				('Phone3_Type'):["Land Line"],
				('Phone2_Type'):["Land Line"],
				('Phone3_Last_Seen'):[],
				('Phone1_DNC'):["NO"],
				('Phone10_Last_Seen'):[],
				('Phone10_Number'):[3175550075],
				('Phone10_Score'):[93],
				('Phone10_Type'):["Land Line"],
				('Phone10_DNC'):["NO"],
				('Phone1_Last_Seen'):[],
				('Phone1_Number'):[3175550074],
				('Phone2_Number'):[3175557430],
				('Phone3_DNC'):["NO"],
				('Phone2_Last_Seen'):[],
				('Phone3_Number'):[3175555508],
		]

		MultiValuedMap<String, Object> map = this.createMultiValuedMapFrom(literal);

		when:
			List<String> keyList = map.keySet()
				// SOURCE: Phind AI
				.sort { a, b ->
					def aNum = a.split('_')[0].replaceAll(/[^0-9]/, '').toInteger()
					def bNum = b.split('_')[0].replaceAll(/[^0-9]/, '').toInteger()
					return aNum <=> bNum ?: a <=> b
				}
		then:
		keyList[0].startsWith("Phone1_")
		keyList[1].startsWith("Phone1_")
		keyList[2].startsWith("Phone1_")
		keyList[3].startsWith("Phone1_")
		keyList[4].startsWith("Phone1_")
	}

	def "ExtractFromRawPhoneData should handle those Phone0_... columns"() {
		setup:

		def literal = [
				('Phone0_DNC'): ['No'],
				("Phone0_Type"): ['Mobile'],
				('Phone0_Number'): ['3175550123'],
				('Phone0_Score'): [100],
				('Phone1_DNC'): ['No'],
				("Phone1_Type"): ['Mobile'],
				('Phone1_Number'): ['3175550157'],
				('Phone1_Score'): [90],
		]

		MultiValuedMap<String, Object> map = this.createMultiValuedMapFrom(literal);

		when:
		List<PhoneModel> phoneModelList = PhoneModel.ExtractFromRawPhoneData(map)

		then:
		phoneModelList[0].getPhoneNumber() == '3175550123'
		phoneModelList[1].getPhoneNumber() == '3175550157'
	}

	def "ExtractPhoneNumber() should be able to handle unknown phone types"() {
		setup:
		def literal = [
				('Phone0_DNC'): ['No'],
				('Phone0_Number'): ['9991234567'],
		]

		MultiValuedMap<String, Object> map = this.createMultiValuedMapFrom(literal);

		when:
		List<PhoneModel> phoneModelList = PhoneModel.ExtractFromRawPhoneData(map);

		then:
		phoneModelList[0].getPhoneType() != null;
	}

	private MultiValuedMap<String, Object> createMultiValuedMapFrom(Map originalMap) {
		MultiValuedMap<String, Object> map = new ArrayListValuedHashMap<>()

		originalMap.each { key, value ->
			if (value.size() == 0) {
				map.put(key, null)
				return
			}

			value.each { item ->
				map.put(key, item)
			}
		}

		return map;
	}

}